package daniel.caixa.dto;

import daniel.caixa.entity.VehicleStatus;
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

