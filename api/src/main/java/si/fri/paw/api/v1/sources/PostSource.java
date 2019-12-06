package si.fri.paw.api.v1.sources;

import com.google.common.io.Files;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.beans.DeleteBean;
import si.fir.paw.utility.beans.ReadBean;
import si.fir.paw.utility.beans.UpdateBean;
import si.fir.paw.utility.dtos.create.PostCreateDTO;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.update.PostUpdateDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Path("posts")
@ApplicationScoped
public class PostSource {

    @Inject
    CreateBean createBean;

    @Inject
    ReadBean readBean;

    @Inject
    UpdateBean updateBean;

    @Inject
    DeleteBean deleteBean;

    private static final Logger log = Logger.getLogger(PostSource.class.getName());


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewPost(@FormDataParam("authorId") int authorId,
                                  @FormDataParam("description") String description,
                                  @FormDataParam("tagNames") String tagNames,
                                  @FormDataParam("rating") String rating,
                                  @FormDataParam("file") InputStream fileInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileMetaData
                                 ){
        try {

            String fileExtension = Files.getFileExtension(fileMetaData.getFileName());

            PostCreateDTO pdto = new PostCreateDTO();
            pdto.setAuthorID(authorId);
            pdto.setTagNames(tagNames.split(" "));
            pdto.setDescription(description);
            pdto.setRating(rating);
            pdto.setFileExtension(fileExtension);
            pdto.setFileInputStream(fileInputStream);


            log.info("Creating new post.");
            PostDTO post = createBean.createNewPost(pdto);

            return Response.status(Response.Status.CREATED).entity(post).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostById(@PathParam("id") int id) {

        PostDTO post = readBean.getPostById(id);

        if (post != null) {
            return Response.status(Response.Status.OK).entity(post).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("search/{tags}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByTags(@PathParam("tags") String tags){
        String[] tagNames = tags.split(" ");

        List<PostDTO> posts = readBean.getPostsByTags(tagNames);
        if (posts != null){
            return Response.status(Response.Status.OK).entity(posts).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("favourite/{id}/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavourite(@PathParam("id") int postId, String jsonString){

        try {

            JSONObject json = new JSONObject(jsonString);

            PostUpdateDTO pdto = new PostUpdateDTO();
            pdto.setEditPostID(postId);
            pdto.setFavouriteEditUserID(json.getInt("userId"));

            PostDTO post = updateBean.newFavouritePost(pdto);

            return Response.status(Response.Status.OK).entity(post).build();
        }
        catch (JSONException je){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(je).build();
        }
    }

    @PUT
    @Path("favourite/{id}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavourite(@PathParam("id") int postId, String jsonString){

        try {

            JSONObject json = new JSONObject(jsonString);

            PostUpdateDTO pdto = new PostUpdateDTO();
            pdto.setEditPostID(postId);
            pdto.setFavouriteEditUserID(json.getInt("userId"));

            PostDTO post = updateBean.removeFavouritePost(pdto);

            return Response.status(Response.Status.OK).entity(post).build();
        }
        catch (JSONException je){
            log.warning("Error parsing json.");
            return Response.status(Response.Status.BAD_REQUEST).entity(je).build();
        }
    }

    @PUT
    @Path("tags/{id}/add/{tags}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTags(@PathParam("id") int postId, @PathParam("tags") String tags){

        PostUpdateDTO pdto = new PostUpdateDTO();
        pdto.setEditPostID(postId);
        pdto.setNewTags(tags.split(" "));

        PostDTO post = updateBean.addTags(pdto);
        if (post != null){
            return Response.status(Response.Status.OK).entity(post).build();
        }

        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("tags/{id}/remove/{tags}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTags(@PathParam("id") int postId, @PathParam("tags") String tags){

        PostUpdateDTO pdto = new PostUpdateDTO();
        pdto.setEditPostID(postId);
        pdto.setTagsToRemove(tags.split(" "));

        PostDTO post = updateBean.removeTags(pdto);
        if (post != null){
            return Response.status(Response.Status.OK).entity(post).build();
        }

        return  Response.status(Response.Status.NOT_FOUND).build();
    }

}