package si.fri.paw.api.v1.sources;

import si.fir.paw.utility.beans.config.ConfigurationBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigSource {

    @Inject
    private ConfigurationBean properties;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getConfigData() {
        String response =
                "{" +
                    "\"Maintenance mode\": \"%b\"," +
                "}";

        response = String.format(
                response,
                properties.isMaintenanceMode());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    @POST
    @Path("maintenance/{mode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMaintenanceMode(@PathParam("mode") boolean mode){

        properties.setMaintenanceMode(mode);

        return Response.status(Response.Status.OK).entity("Maintenance mode set to: " + mode).build();
    }
}
