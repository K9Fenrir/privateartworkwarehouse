package si.fri.paw.api.v1.ExceptionMappers;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.Exceptions.InvalidParameterException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidParameterExceptionMapper implements ExceptionMapper<InvalidParameterException> {

    @Override
    public Response toResponse(InvalidParameterException exception)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("message", exception.getMessage());
            json.put("errorCode", 400);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(json.toString()).build();
    }

}
