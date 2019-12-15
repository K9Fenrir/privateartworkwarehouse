package si.fri.paw.api.v1.sources;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.security.AuthenticationBean;
import si.fir.paw.utility.beans.security.AuthorizationBean;
import si.fir.paw.utility.beans.service.ReadBean;
import si.fir.paw.utility.dtos.read.UserDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("login")
@ApplicationScoped
public class LoginSource {

    @Inject
    AuthorizationBean authorizationBean;

    @Inject
    AuthenticationBean authenticationBean;

    @Inject
    ReadBean readBean;

    private static final Logger log = Logger.getLogger(TagSource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonString) throws InvalidParameterException, PersistenceException {
        try{
            JSONObject json = new JSONObject(jsonString);

            String username = json.getString("username");
            String password = json.getString("password");

            if (!authenticationBean.authenticate(username, password)){

                JSONObject errorJson = new JSONObject(jsonString);
                errorJson.put("message", "Incorrect username or password");
                errorJson.put("status", 401);

                return Response.status(Response.Status.UNAUTHORIZED).entity(errorJson.toString()).build();
            }

            UserDTO user = readBean.getUserByUsername(username);

            String jwt = authorizationBean.generateJWT(user.getUsername(), user.isAdmin());

            return Response.status(Response.Status.ACCEPTED).entity(jwt).build();
        }
        catch (JSONException je){
            throw new InvalidParameterException("Request JSON is invalid");
        }
    }

}

