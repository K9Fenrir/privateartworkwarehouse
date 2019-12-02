package si.fri.paw.api.v1.sources;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.beans.DeleteBean;
import si.fir.paw.utility.beans.ReadBean;
import si.fir.paw.utility.beans.UpdateBean;
import si.fir.paw.utility.dtos.create.UserCreateDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fir.paw.utility.dtos.delete.UserDeleteDTO;
import si.fir.paw.utility.dtos.update.UserUpdateDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("users")
@ApplicationScoped
public class UserSource {

    @Inject
    CreateBean createBean;

    @Inject
    ReadBean readBean;

    @Inject
    UpdateBean updateBean;

    @Inject
    DeleteBean deleteBean;

    private static final Logger log = Logger.getLogger(UserSource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(String jsonString){

        try {
            JSONObject json = new JSONObject(jsonString);

            UserCreateDTO udto = new UserCreateDTO();
            udto.setUsername(json.getString("username"));
            udto.setEmail(json.getString("email"));

            UserDTO user = createBean.createNewUser(udto);

            return Response.status(Response.Status.CREATED).entity(user).build();

        }
        catch (JSONException jsne){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsne).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(){

        List<UserDTO> users = readBean.getAllUsers();

        if (users != null){
            return Response.status(Response.Status.OK).entity(users).build();
        }
        else{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id){

        UserDTO user = readBean.getUserByID(id);

        if (user != null) {
            return Response.status(Response.Status.OK).entity(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserById(@PathParam("id") int id, String jsonString){

        try {
            JSONObject json = new JSONObject(jsonString);

            UserUpdateDTO udto = new UserUpdateDTO();
            udto.setId(id);
            udto.setNewEmail(json.getString("email"));

            UserDTO user = updateBean.updateUserEmail(udto);

            return Response.status(Response.Status.OK).entity(user).build();
        }
        catch (JSONException jsne){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsne.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeAdminStatus(@PathParam("id") int id, String jsonString){

        try {
            JSONObject json = new JSONObject(jsonString);

            UserUpdateDTO udto = new UserUpdateDTO();
            udto.setId(id);
            udto.setAdminStatus(json.getBoolean("adminStatus"));

            UserDTO user = updateBean.updateUserAdminStatus(udto);

            return Response.status(Response.Status.OK).entity(user).build();
        }
        catch (JSONException jsne){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsne.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteUserById(@PathParam("id") int id){

        UserDeleteDTO udto = new UserDeleteDTO();
        udto.setToDeleteID(id);

        if (deleteBean.deleteUser(udto)){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
