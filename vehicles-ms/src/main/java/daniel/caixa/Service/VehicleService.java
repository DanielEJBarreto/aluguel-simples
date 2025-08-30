package daniel.caixa.Service;

import daniel.caixa.DTO.VehicleRequest;
import daniel.caixa.DTO.VehicleResponse;
import daniel.caixa.Entity.Vehicle;
import daniel.caixa.Entity.VehicleStatus;
import daniel.caixa.Exception.VehicleCanOnlyBeRentedIfAvailableException;
import daniel.caixa.Exception.VehicleNotFoundException;
import daniel.caixa.Exception.VehicleRentedOrUnderMaintenanceException;
import daniel.caixa.Mapper.VehicleMapper;
import daniel.caixa.Repository.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleService {

    @Inject
    VehicleRepository repository;

    @Inject
    VehicleMapper mapper;

    public List<VehicleResponse> listAll() {
        return repository.listAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse findById(Long id) {
        return repository.findByIdOptional(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new VehicleNotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public VehicleResponse create(VehicleRequest dto) {
        Vehicle entity = mapper.toEntity(dto);
        repository.persist(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = repository.findByIdOptional(id)
                .orElseThrow(() -> new VehicleNotFoundException("Veículo não encontrado"));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new VehicleRentedOrUnderMaintenanceException("Veículo alugado ou em manutenção não pode ser excluído");
        }

        repository.delete(vehicle);
    }


    @Transactional
    public VehicleResponse updateStatus(Long id, VehicleStatus newStatus) {
        Vehicle vehicle = repository.findByIdOptional(id)
                .orElseThrow(() -> new VehicleNotFoundException("Veículo não encontrado"));

//        RENTED passou para booking-ms
//        // Regra de negócio: só pode alugar se estiver disponível
//        if (newStatus == VehicleStatus.RENTED && vehicle.getStatus() != VehicleStatus.AVAILABLE) {
//            throw new VehicleCanOnlyBeRentedIfAvailableException("Veículo só pode ser alugado se estiver disponível");
//        }

        vehicle.setStatus(newStatus);
        return mapper.toResponse(vehicle);
    }


}

