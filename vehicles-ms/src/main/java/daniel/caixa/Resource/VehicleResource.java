package daniel.caixa.resource;

import daniel.caixa.dto.VehicleRequest;
import daniel.caixa.dto.VehicleResponse;
import daniel.caixa.dto.VehicleStatusUpdateRequest;
import daniel.caixa.Service.VehicleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"admin", "user"})
public class VehicleResource {

    @Inject
    VehicleService service;

    @GET
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<VehicleResponse> listAll() {
        return service.listAll();
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public VehicleResponse findById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @POST
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid VehicleRequest dto) {
        VehicleResponse created = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
//        return Response.noContent().build();
        return Response.noContent().build();
    }

    @PATCH
    @RolesAllowed("admin")
    @Path("/{id}/status")
    public VehicleResponse updateStatus(@PathParam("id") Long id,
                                        @Valid VehicleStatusUpdateRequest request) {
        return service.updateStatus(id, request.getStatus());
    }

}

