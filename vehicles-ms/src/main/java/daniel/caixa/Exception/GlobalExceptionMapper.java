package daniel.caixa.Exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        ErrorResponse error;

        if (e instanceof VehicleNotFoundException) {
            error = new ErrorResponse(LocalDateTime.now(), "404", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if (e instanceof VehicleUnavailableException) {
            error = new ErrorResponse(LocalDateTime.now(),"409", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        if (e instanceof VehicleRentedOrUnderMaintenanceException) {
            error = new ErrorResponse(LocalDateTime.now(),"400", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (e instanceof VehicleCanOnlyBeRentedIfAvailableException) {
            error = new ErrorResponse(LocalDateTime.now(),"400", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // fallback genérico
        error = new ErrorResponse(LocalDateTime.now(), "400", e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}