package daniel.caixa.exception;

public class InvalidCustomerException extends RuntimeException{
    public InvalidCustomerException(String message){
        super(message);
    }
}
