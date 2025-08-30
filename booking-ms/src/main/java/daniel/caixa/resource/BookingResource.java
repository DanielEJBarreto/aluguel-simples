package daniel.caixa.resource;

import daniel.caixa.dto.AlterBookingStatusRequest;
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

import static org.keycloak.util.JsonSerialization.mapper;

@ApplicationScoped
@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
public class BookingResource {

    BookingService bookingService;

    BookingResponse bookingResponse;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Inject
    JsonWebToken jwt;

    @Inject
    BookingMapper mapper;

    @GET
    @Path("/listall")
    public List<BookingResponse> listAll() {
        return bookingService.listAll();
    }

    @GET
    @Path("/{id}")
    public BookingResponse findById(@PathParam("id") Long id) {
        return bookingService.findById(id);
    }

//    @GET
//    @RolesAllowed("user")
//    @Path("/vehicle/{vehicleId}")
//    public Response getBookingByVehicleId(@PathParam("vehicleId") Long vehicleId) {
//        BookingResponse booking = bookingService.getActiveBookingByVehicleId(vehicleId);
//
//        if (booking == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("No active booking found for vehicle " + vehicleId)
//                    .build();
//        }
//
//        return Response.ok(booking).build();
//    }

    @GET
    @RolesAllowed("user")
    @Path("/mybookings")
    public Response listMyBookings() {
        String customerId = jwt.getSubject();
        List<Booking> bookings = bookingService.listAllForCustomer(customerId);
        List<BookingResponse> responses = bookings.stream()
                .map(mapper::toResponse)
                .toList();
        return Response.ok(responses).build();
    }

    @POST
    @RolesAllowed("user")
    public Response create(@Valid BookingRequest dto) {
        String customerId = jwt.getSubject();
        BookingResponse created = bookingService.create(dto, customerId);
        return Response.status(Response.Status.CREATED).entity(bookingResponse).build();
    }

    @PATCH
    @RolesAllowed("user")
    @Path("/{id}/alter")
    public Response alter(@PathParam("id") Long id, AlterBookingStatusRequest dto) {
        bookingService.alter(id, dto.getStatus());
        return Response.ok().build();
    }

}

