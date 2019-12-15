package si.fri.paw.api.v1.filters;

import si.fir.paw.utility.beans.config.ConfigurationBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class MaintenanceFilter implements ContainerRequestFilter {

    @Inject
    private ConfigurationBean properties;

    @Context
    private UriInfo info;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws WebApplicationException {

        if (!info.getAbsolutePath().getPath().contains("config/maintenance") && properties.isMaintenanceMode()){
            throw new WebApplicationException(Response.status(Response.Status.SERVICE_UNAVAILABLE).build());
        }

    }
}
