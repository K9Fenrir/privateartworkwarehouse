package si.fri.paw.api.v1.ExceptionMappers;

import javassist.expr.Instanceof;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PersistanceExceptionMapper implements ExceptionMapper<PersistenceException> {

    @Override
    public Response toResponse(PersistenceException exception)
    {
        JSONObject json = new JSONObject();

        try {

            json.put("message", exception.getMessage());

            if (exception instanceof EntityExistsException) {
                json.put("status", 409);
            }
            else if (exception instanceof EntityNotFoundException || exception instanceof NoResultException){
                json.put("status", 404);
            }
            else {
                json.put("status", 500);
            }

            return Response.status(json.getInt("status")).entity(json.toString()).build();

        }


        catch (JSONException e){
            e.printStackTrace();
            String jsonString = "{\"message\":\"There was an error processing your request.\"}";

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonString).build();
        }

    }

}
