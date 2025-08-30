package daniel.caixa.Exception;

public class VehicleCanOnlyBeRentedIfAvailableException extends RuntimeException{
    public VehicleCanOnlyBeRentedIfAvailableException(String message) {
        super(message);
    }
}
