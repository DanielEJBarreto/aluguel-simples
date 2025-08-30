package daniel.caixa.DTO;

import daniel.caixa.Entity.VehicleStatus;
import jakarta.validation.constraints.NotNull;

public class VehicleStatusUpdateRequest {

    @NotNull
    private VehicleStatus status;

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}

