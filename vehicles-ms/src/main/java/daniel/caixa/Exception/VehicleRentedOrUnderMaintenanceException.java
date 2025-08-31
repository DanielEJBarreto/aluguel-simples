package daniel.caixa.exception;

public class VehicleRentedOrUnderMaintenanceException extends RuntimeException {
    public VehicleRentedOrUnderMaintenanceException(String message) {
        super(message);
    }
}
