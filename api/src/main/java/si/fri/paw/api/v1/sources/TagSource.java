package si.fri.paw.api.v1.sources;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.beans.DeleteBean;
import si.fir.paw.utility.beans.ReadBean;
import si.fir.paw.utility.beans.UpdateBean;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTag(String jsonString){

        try {
            JSONObject json = new JSONObject(jsonString);

            TagCreateDTO tdto = new TagCreateDTO();
            tdto.setName(json.getString("name"));
            tdto.setDescription(json.getString("description"));
            tdto.setType(json.getString("type"));

            TagDTO tag = createBean.createNewTag(tdto);

            return Response.status(Response.Status.CREATED).entity(tag).build();
        }
        catch (JSONException jsne){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsne).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTags(){

        List<TagDTO> tags = readBean.getAllTags();

        if (tags != null) {
            return Response.status(Response.Status.OK).entity(tags).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

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

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTagById(@PathParam("id") String id, String jsonString){

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
