package daniel.caixa.resource;

import daniel.caixa.dto.BookingRequest;
import daniel.caixa.dto.BookingResponse;
import daniel.caixa.entity.Booking;
import daniel.caixa.mapper.BookingMapper;
import daniel.caixa.service.BookingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;

@ApplicationScoped
@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @Inject
    BookingService bookingService;

    BookingResponse bookingResponse;

    @Inject
    JsonWebToken jwt;

    @Inject
    BookingMapper mapper;

    @GET
    @RolesAllowed("admin")
    @Path("/listall")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookingResponse> listAll() {
        return bookingService.listAll();
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/mybookings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMyBookings() {
        String customerId = jwt.getSubject();
        List<Booking> bookings = bookingService.listAllForCustomer(customerId);
        List<BookingResponse> responses = bookings.stream()
                .map(mapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @POST
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(@Valid BookingRequest dto) {
        String customerId = jwt.getSubject();
        BookingResponse created = bookingService.create(dto, customerId);
//        return Response.status(Response.Status.CREATED).entity(bookingResponse).build();
        return Response.status(Response.Status.CREATED).entity("Reserva criada com sucesso!").build();
    }

    @PATCH
    @RolesAllowed({"admin", "user"})
    @Path("/{id}/cancelBooking")
    @Produces(MediaType.TEXT_PLAIN)
    public Response alter(@PathParam("id") Long id) {
        bookingService.cancelBooking(id);
        return Response.ok().entity("Reserva cancelada com sucesso!").build();
    }

    @PATCH
    @RolesAllowed({"admin", "user"})
    @Path("/{id}/check-in")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkIn(@PathParam("id") Long id){
        String customerId = jwt.getSubject();
        bookingService.vehicleCheckIn(id, customerId);
        return Response.ok().entity("Check-in iniciado com sucesso!").build();
    }

    @PATCH
    @RolesAllowed({"admin", "user"})
    @Path("/{id}/check-out")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkOut(@PathParam("id") Long id){
        String customerId = jwt.getSubject();
        bookingService.vehicleCheckOut(id, customerId);
        return Response.ok().entity("Check-out realizado com sucesso!").build();
    }

}

