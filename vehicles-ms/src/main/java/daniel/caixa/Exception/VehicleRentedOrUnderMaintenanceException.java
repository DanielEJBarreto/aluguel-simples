package daniel.caixa.Exception;

public class VehicleRentedOrUnderMaintenanceException extends RuntimeException {
    public VehicleRentedOrUnderMaintenanceException(String message) {
        super(message);
    }
}
