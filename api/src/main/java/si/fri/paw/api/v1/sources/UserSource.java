package si.fri.paw.api.v1.sources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.service.CreateBean;
import si.fir.paw.utility.beans.service.DeleteBean;
import si.fir.paw.utility.beans.service.ReadBean;
import si.fir.paw.utility.beans.service.UpdateBean;
import si.fir.paw.utility.dtos.create.UserCreateDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fir.paw.utility.dtos.delete.UserDeleteDTO;
import si.fir.paw.utility.dtos.update.UserUpdateDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.PersistenceException;
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

    @Operation(description = "Create new user", summary = "Create user", tags = "Users", responses = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created new user",
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Failed to create user due to invalid parameters"
            )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(UserCreateDTO udto) throws InvalidParameterException, PersistenceException {

            UserDTO user = createBean.createNewUser(udto);

            return Response.status(Response.Status.CREATED).entity(user).build();

    }

    @Operation(description = "Get all users", summary = "Get users", tags = "Users", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved all users",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Failed to retrieve users due to server error"
            )
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {

        List<UserDTO> users = readBean.getAllUsers();

        if (users != null) {
            return Response.status(Response.Status.OK).entity(users).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Get user by ID", summary = "Get user", tags = "Users", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved specified user",
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            )
    })
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("id") String username) throws PersistenceException {

        UserDTO user = readBean.getUserByUsername(username);

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @Operation(description = "Update information of specified user", summary = "Update user", tags = "Users", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Failed to update user due to invalid parameters"
            )
    })
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserEmail(@PathParam("id") String username, String jsonString) throws InvalidParameterException, PersistenceException {

        try {
            JSONObject json = new JSONObject(jsonString);

            UserUpdateDTO udto = new UserUpdateDTO();
            udto.setUsername(username);
            udto.setNewEmail(json.getString("email"));

            udto.setUsername(username);
            UserDTO user = updateBean.updateUserEmail(udto);

            return Response.status(Response.Status.OK).entity(user).build();

        } catch (Exception e) {
            log.warning("Error parsing json.");
            throw new InvalidParameterException("Request JSON is invalid");
        }
    }

    @Operation(description = "Change admin status of specified user", summary = "Update user admin", tags = "Users", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Failed to update user due to invalid parameters"
            )
    })
    @PUT
    @Path("{id}/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeAdminStatus(@PathParam("username") String username, String jsonString) {

        try {
            JSONObject json = new JSONObject(jsonString);

            UserUpdateDTO udto = new UserUpdateDTO();
            udto.setUsername(username);
            udto.setAdminStatus(json.getBoolean("adminStatus"));

            UserDTO user = updateBean.updateUserAdminStatus(udto);

            return Response.status(Response.Status.OK).entity(user).build();
        } catch (JSONException jsne) {
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsne.getMessage()).build();
        }
    }

    @Operation(description = "Delete user by ID", summary = "Delete user", tags = "Users", responses = {
            @ApiResponse(responseCode = "204",
                    description = "User successfully deleted"
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
    })
    @DELETE
    @Path("{id}")
    public Response deleteUserById(@PathParam("username") String username) throws PersistenceException {

        UserDeleteDTO udto = new UserDeleteDTO();
        udto.setUsername(username);

        deleteBean.deleteUser(udto);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
