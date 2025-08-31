package daniel.caixa.exception;

public class VehicleCanOnlyBeRentedIfAvailableException extends RuntimeException{
    public VehicleCanOnlyBeRentedIfAvailableException(String message) {
        super(message);
    }
}
