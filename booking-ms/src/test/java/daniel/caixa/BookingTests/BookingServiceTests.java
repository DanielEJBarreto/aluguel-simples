//package daniel.caixa.BookingTests;
//
//import daniel.caixa.DTO.BookingRequest;
//import daniel.caixa.DTO.BookingResponse;
//import daniel.caixa.Entity.Booking;
//import daniel.caixa.Entity.BookingStatus;
//import daniel.caixa.Exception.InvalidReservationDateException;
//import daniel.caixa.Exception.InvalidReservationStatusException;
//import daniel.caixa.Exception.VehicleUnavailableException;
//import daniel.caixa.Mapper.BookingMapper;
//import daniel.caixa.Repository.BookingRepository;
//import daniel.caixa.Service.BookingService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookingServiceTests {
//
//    @InjectMocks
//    private BookingService bookingService;
//
//    @Mock
//    private VehicleRepository vehicleRepository;
//
//    @Mock
//    private BookingRepository bookingRepository;
//
//    @Mock
//    private BookingMapper bookingMapper;
//
//    private Vehicle mockAvailableVehicle() {
//        Vehicle vehicle = new Vehicle();
//        vehicle.setId(1L);
//        vehicle.setStatus(VehicleStatus.AVAILABLE);
//        return vehicle;
//    }
//
//    private BookingRequest mockBookingRequest(LocalDate start, LocalDate end) {
//        BookingRequest request = new BookingRequest();
//        request.setVehicleId(1L);
//        request.setCustomerId("Daniel");
//        request.setStartDate(start);
//        request.setEndDate(end);
//        request.setStatus(BookingStatus.CREATED);
//        return request;
//    }
//
//    @Test
//    public void testCreateBookingWithValidDatesAndAvailableVehicle_shouldSucceed() {
//        // 1. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//
//        // 2. Simular dados válidos de reserva
//        BookingRequest request = mockBookingRequest
//                (LocalDate.now().plusDays(1), LocalDate.now().plusDays(7));
//
//        // 3. Preparar entidade e DTO de retorno simulados
//        Booking bookingEntity = new Booking();
//        BookingResponse bookingResponse = new BookingResponse();
//
//        when(bookingMapper.toEntity(request)).thenReturn(bookingEntity);
//        when(bookingMapper.toResponse(bookingEntity)).thenReturn(bookingResponse);
//
//        // 4. Executar método testado
//        BookingResponse result = bookingService.create(request);
//
//        // 5. Verificações
//        verify(vehicleRepository).findById(1L);
//        verify(bookingRepository).persist(bookingEntity);
//        verify(bookingMapper).toEntity(request);
//        verify(bookingMapper).toResponse(bookingEntity);
//
//        assertEquals(bookingResponse, result);
//    }
//
//    @Test
//    public void testCreateBookingWithStartDateInThePast() {
//        // 1. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//
//        // 2. Simular dados válidos de reserva
//        BookingRequest request = mockBookingRequest
//                (LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
//
//        // 3. Espera falha
//        Assertions.assertThrows(InvalidReservationDateException.class, () -> bookingService.create(request));
//
//        // 4. Verifica se o método parou cedo e não persistiu
//        verify(bookingRepository, never()).persist((Booking) any());
//    }
//
//    @Test
//    public void testCreateBookingWIthEndDateBeforeStartDate() {
//        // 1. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//
//        // 2. Simular dados válidos de reserva
//        BookingRequest request = mockBookingRequest
//                (LocalDate.now().plusDays(10), LocalDate.now().plusDays(7));
//
//        // 3. Espera falha
//        Assertions.assertThrows(InvalidReservationDateException.class, () -> bookingService.create(request));
//
//        // 4. Verifica se o método parou cedo e não persistiu
//        verify(bookingRepository, never()).persist((Booking) any());
//    }
//
//    @Test
//    public void testCreateBookingWithVehicleUnAvailable() {
//        // 1. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//        vehicle.setStatus(VehicleStatus.RENTED);
//
//        // 2. Simular dados inválidos de reserva
//        BookingRequest request = mockBookingRequest
//                (LocalDate.now().plusDays(1), LocalDate.now().plusDays(7));
//
//        // 3. Espera falha
//        Assertions.assertThrows(VehicleUnavailableException.class, () -> bookingService.create(request));
//
//        // 4. Verifica se o método parou cedo e não persistiu
//        verify(bookingRepository, never()).persist((Booking) any());
//    }
//
//    @Test
//    public void testCancelBookingWithStatusCREATED() {
//        // 1. Simular reserva existente com status CREATED
//        Booking booking = new Booking();
//        booking.setId(1L);
//        booking.setVehicleId(1L);
//        booking.setStatus(BookingStatus.CREATED);
//
//        when(bookingRepository.findByIdOptional(1L)).thenReturn(Optional.of(booking));
//
//        // 2. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//
//        // 3. Executar método de cancelamento
//        bookingService.alter(1L, BookingStatus.CANCELED);
//
//        // 4. Verificações
//        assertEquals(BookingStatus.CANCELED, booking.getStatus());
//        verify(bookingRepository).findByIdOptional(1L);
//    }
//
//    @Test
//    public void testCancelBookingWithStatusCANCELEDorFINISHED() {
//        // 1. Simular reserva existente com status CREATED
//        Booking booking = new Booking();
//        booking.setId(1L);
//        booking.setStatus(BookingStatus.FINISHED);//or CANCELED
//
//        when(bookingRepository.findByIdOptional(1L)).thenReturn(Optional.of(booking));
//
//        // 2. Espera falha
//        Assertions.assertThrows(InvalidReservationStatusException.class, () -> {
//            bookingService.alter(booking.getId(), BookingStatus.FINISHED);//or CANCELED
//        });
//
//        // 3. Verificações
//        assertTrue(booking.getStatus() == BookingStatus.CANCELED ||
//                   booking.getStatus() == BookingStatus.FINISHED);
//        verify(bookingRepository, never()).persist((Booking) any());
//    }
//
//    @Test
//    public void testFinishBookingWithStatusCREATED() {
//        // 1. Simular reserva existente com status CREATED
//        Booking booking = new Booking();
//        booking.setId(1L);
//        booking.setVehicleId(1L);
//        booking.setStatus(BookingStatus.CREATED);
//
//        when(bookingRepository.findByIdOptional(1L)).thenReturn(Optional.of(booking));
//
//        // 2. Simular veículo disponível
//        Vehicle vehicle = mockAvailableVehicle();
//        when(vehicleRepository.findById(1L)).thenReturn(vehicle);
//
//        // 3. Executar alteração de status
//        bookingService.alter(1L, BookingStatus.FINISHED);
//
//        // 4. Verificações
//        assertEquals(BookingStatus.FINISHED, booking.getStatus());
//        verify(bookingRepository).findByIdOptional(1L);
//        verify(vehicleRepository).findById(1L);
//    }
//
//    @Test
//    public void testFinishBookingAlreadyCanceledOrFinished() {
//        // 1. Simular reserva existente com status CREATED
//        Booking booking = new Booking();
//        booking.setId(1L);
//        booking.setStatus(BookingStatus.CANCELED);// or FINISHED
//
//        when(bookingRepository.findByIdOptional(1L)).thenReturn(Optional.of(booking));
//
//        // 2. Espera falha
//        Assertions.assertThrows(InvalidReservationStatusException.class, () -> {
//            bookingService.alter(booking.getId(), BookingStatus.CANCELED);// or FINISHED
//        });
//
//        // 3. Verificações
//        assertTrue(booking.getStatus() == BookingStatus.CANCELED ||
//                   booking.getStatus() == BookingStatus.FINISHED);
//        verify(bookingRepository).findByIdOptional(1L);
//    }
//
//}
