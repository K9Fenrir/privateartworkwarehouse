package si.fri.paw.api.v1.sources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.jose4j.jwt.JwtClaims;
import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.security.AuthorizationBean;
import si.fir.paw.utility.beans.service.CreateBean;
import si.fir.paw.utility.beans.service.DeleteBean;
import si.fir.paw.utility.beans.service.ReadBean;
import si.fir.paw.utility.beans.service.UpdateBean;
import si.fir.paw.utility.dtos.create.TagCreateDTO;
import si.fir.paw.utility.dtos.delete.TagDeleteDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.update.TagUpdateDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("tags")
@ApplicationScoped
public class TagSource {

    @Inject
    CreateBean createBean;

    @Inject
    ReadBean readBean;

    @Inject
    UpdateBean updateBean;

    @Inject
    DeleteBean deleteBean;

    private static final Logger log = Logger.getLogger(TagSource.class.getName());


    @Operation(description = "Create new tag", summary = "Create tag", tags = "Tags", responses = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created new tag",
                    content = @Content(
                            schema = @Schema(implementation = TagDTO.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Failed to create tag due to invalid parameters"
            )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTag(TagCreateDTO tdto) throws InvalidParameterException {


        TagDTO tag = createBean.createNewTag(tdto);

        return Response.status(Response.Status.CREATED).entity(tag).build();
    }

    @Operation(description = "Get all tags", summary = "Get all tags", tags = "Tags", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved specified tag",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = TagDTO.class)))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Unable to retrieve tags"
            )
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTags(){

        List<TagDTO> tags = readBean.getAllTags();

        if (tags != null) {
            return Response.status(Response.Status.OK).entity(tags).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(description = "Get tag by name", summary = "Get tag", tags = "Tags", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved specified tag",
                    content = @Content(
                            schema = @Schema(implementation = TagDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid parameters"
            )
    })
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagById(@PathParam("id") String id){

        TagDTO tag = readBean.getTagByName(id);

        if (tag != null){
            return Response.status(Response.Status.OK).entity(tag).build();
        }

        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @Operation(description = "Update tag by name", summary = "Update tag", tags = "Tags", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updated specified tag",
                    content = @Content(
                            schema = @Schema(implementation = TagDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "tag not found"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid parameters"
            )
    })
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTagById(@PathParam("id") String id, String jsonString) throws InvalidParameterException{

        try {
            JSONObject json = new JSONObject(jsonString);

            TagUpdateDTO tdto = new TagUpdateDTO();
            tdto.setId(id);
            tdto.setDescription(json.getString("description"));
            tdto.setType(json.getString("type"));

            TagDTO tag = updateBean.updateTag(tdto);

            return Response.status(Response.Status.CREATED).entity(tag).build();
        }
        catch (JSONException je){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(je.getMessage()).build();
        }
        catch (PersistenceException pe){
            log.warning("Persistence exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(pe.getMessage()).build();
        }
    }

    @Operation(description = "Delete tag by name", summary = "Delete tag", tags = "Tags", responses = {
            @ApiResponse(responseCode = "204",
                    description = "Deleted specified tag"
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(responseCode = "401",
                    description = "User not authorized"
            )
    })
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTagById(@PathParam("id") String id){

        TagDeleteDTO tdto = new TagDeleteDTO();
        tdto.setId(id);

        if (deleteBean.deleteTag(tdto)){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
