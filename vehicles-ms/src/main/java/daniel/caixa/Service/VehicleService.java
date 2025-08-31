package daniel.caixa.Service;

import daniel.caixa.dto.VehicleRequest;
import daniel.caixa.dto.VehicleResponse;
import daniel.caixa.entity.Vehicle;
import daniel.caixa.entity.VehicleStatus;
import daniel.caixa.exception.VehicleNotFoundException;
import daniel.caixa.exception.VehicleRentedOrUnderMaintenanceException;
import daniel.caixa.mapper.VehicleMapper;
import daniel.caixa.repository.VehicleRepository;
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

        vehicle.setStatus(newStatus);
        return mapper.toResponse(vehicle);
    }


}

