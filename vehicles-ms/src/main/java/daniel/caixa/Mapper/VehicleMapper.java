package daniel.caixa.Mapper;

import daniel.caixa.DTO.VehicleRequest;
import daniel.caixa.DTO.VehicleResponse;
import daniel.caixa.Entity.Vehicle;
import daniel.caixa.Entity.VehicleStatus;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequest dto) {
        Vehicle v = new Vehicle();
        v.setBrand(dto.getBrand());
        v.setModel(dto.getModel());
        v.setYear(dto.getYear());
        v.setEngine(dto.getEngine());
        v.setStatus(VehicleStatus.AVAILABLE);
        return v;
    }

    public VehicleResponse toResponse(Vehicle v) {
        VehicleResponse dto = new VehicleResponse();
        dto.setId(v.getId());
        dto.setBrand(v.getBrand());
        dto.setModel(v.getModel());
        dto.setYear(v.getYear());
        dto.setEngine(v.getEngine());
        dto.setStatus(v.getStatus());

        // Monta o carTitle
        String title = String.format("%s %s %s", v.getBrand(), v.getModel(), v.getEngine());
        dto.setCarTitle(title);

        return dto;
    }
}
