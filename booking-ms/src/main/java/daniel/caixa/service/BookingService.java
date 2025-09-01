package daniel.caixa.service;

import daniel.caixa.client.VehicleAPIClient;
import daniel.caixa.dto.BookingRequest;
import daniel.caixa.dto.BookingResponse;
import daniel.caixa.entity.Booking;
import daniel.caixa.entity.BookingStatus;
import daniel.caixa.exception.*;
import daniel.caixa.mapper.BookingMapper;
import daniel.caixa.repository.BookingRepository;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository repository;

    @Inject
    BookingMapper mapper;

    @Inject
    @RestClient
    VehicleAPIClient vehicleAPIClient;

    @CacheResult(cacheName = "vehicle-list-cache")
    public List<BookingResponse> listAll() {
        System.out.println(">>>>>>>>>>>>>>Executando busca no banco<<<<<<<<<<<<<<");
        return repository.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @CacheInvalidateAll(cacheName = "vehicle-list-cache")
    @Transactional
    public BookingResponse create(BookingRequest dto, String customerId) {

        VehicleAPIClient.Vehicle vehicle = vehicleAPIClient.findVehicleById(dto.getVehicleId());

        // Regra 1: verificar se o veículo existe e está disponível
        if (vehicle == null) {
            throw new VehicleNotFoundException("Veículo não encontrado");
        }

        if (!vehicle.status().equals("AVAILABLE")) {
            throw new VehicleUnavailableException("Veículo indisponível para reserva");
        }

        // Regra 2: startDate deve ser hoje ou futuro
        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new InvalidReservationDateException("A data de início da reserva deve ser hoje ou no futuro");
        }

        // Regra 3: endDate ≥ startDate
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidReservationDateException("A data de término da reserva deve ser igual ou posterior à data de início");
        }

        // Regra 4: verificar conflito de datas de reserva
        boolean hasConflict = repository.count(
                "vehicleId = ?1 and status in ?2 and startDate <= ?3 and endDate >= ?4",
                dto.getVehicleId(),
                List.of(BookingStatus.RENTED, BookingStatus.CREATED),
                dto.getEndDate(),
                dto.getStartDate()
        ) > 0;

        if (hasConflict) {
            throw new InvalidReservationDateException("Já existe uma reserva para este veículo no período informado");
        }

        // Mapeamento e persistência
        Booking entity = mapper.toEntity(dto, customerId);
        repository.persist(entity);
        return mapper.toResponse(entity);

    }

    @CacheInvalidateAll(cacheName = "vehicle-list-cache")
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = repository.findByIdOptional(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        // Regra 1: Só cancelar se Criada
        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new InvalidReservationStatusException("Booking already " + booking.getStatus());
        }

        //Alterando Status do Veiculo para AVAILABLE
        VehicleAPIClient.Vehicle vehicle = vehicleAPIClient.findVehicleById(booking.getVehicleId());
        vehicleAPIClient.updateStatus(booking.getVehicleId(), new VehicleAPIClient.Vehicle("AVAILABLE"));

        //Alterando Status do Booking para novo Status
        booking.setStatus(BookingStatus.CANCELED);
        booking.setCanceledAt(LocalDate.now());
    }

    public List<Booking> listAllForCustomer(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    //Realiza o check-in
    @CacheInvalidateAll(cacheName = "vehicle-list-cache")
    @Transactional
    public void vehicleCheckIn(Long bookingId, String customerId) {
        Booking booking = repository.findByIdOptional(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        BookingStatus currentStatus = booking.getStatus();

        // Regra 1: Só faz check-in se Criada
        if (currentStatus != BookingStatus.CREATED) {
            throw new InvalidReservationStatusException("Booking " + booking.getStatus() +
                    " not available to check-in");
        }

        //Checa se o customer da reserva é o mesmo do check-in
        if (!booking.getCustomerId().equals(customerId)) {
            throw new InvalidCustomerException("Reservation n# " + bookingId + " not for logged customer!");
        }

        //Alterando status para ACTIVE
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setActivatedAt(LocalDate.now());
    }

    //Realiza o check-out
    @CacheInvalidateAll(cacheName = "vehicle-list-cache")
    @Transactional
    public void vehicleCheckOut(Long bookingId, String customerId) {
        Booking booking = repository.findByIdOptional(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        BookingStatus currentStatus = booking.getStatus();

        // Regra 1: Só faz check-out se Active
        if (currentStatus != BookingStatus.ACTIVE) {
            throw new InvalidReservationStatusException("Booking " + booking.getStatus() +
                    " not available to check-out");
        }

        //Checa se o customer da reserva é o mesmo do check-out
        if (!booking.getCustomerId().equals(customerId)) {
            throw new InvalidCustomerException("Reservation n# " + bookingId + " not for logged customer!");
        }

        //Alterando status para FINISHED
        booking.setStatus(BookingStatus.FINISHED);
        booking.setFinishedAt(LocalDate.now());
    }
}