package daniel.caixa.Resource;

import daniel.caixa.DTO.VehicleRequest;
import daniel.caixa.DTO.VehicleResponse;
import daniel.caixa.DTO.VehicleStatusUpdateRequest;
import daniel.caixa.Service.VehicleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"admin", "user"})
public class VehicleResource {

    @Inject
    VehicleService service;

    @GET
    public List<VehicleResponse> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{id}")
    public VehicleResponse findById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid VehicleRequest dto) {
        VehicleResponse created = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/status")
    public VehicleResponse updateStatus(@PathParam("id") Long id,
                                        @Valid VehicleStatusUpdateRequest request) {
        return service.updateStatus(id, request.getStatus());
    }

}

