package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostEditDTO;
import si.fir.paw.utility.dtos.TagEditDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class EditBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(EditBean.class.getName());

    public Post newFavouritePost(PostEditDTO pdto){
        Post editedPost = postBean.addNewFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        if (editedPost != null){
            log.info("Added new favourite: Post " + pdto.getEditPostID() + " to User " + pdto.getFavouriteEditUserID());
        }
        else{
            log.info("Failed to add favourite to post " + pdto.getEditPostID() + ": post or user does not exist");
        }

        return editedPost;
    }

    public Post removeFavouritePost(PostEditDTO pdto){
        Post editedPost = postBean.removeFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        if (editedPost != null) {
            log.info("Removed favourite: Post " + editedPost.getId() + " from User " + pdto.getFavouriteEditUserID());
        }
        else{
            log.info("Failed to remove favourite from post " + pdto.getEditPostID() + ": post or user does not exist");
        }

        return editedPost;
    }

    public Post addTags(PostEditDTO pdto){
        Post editedPost = postBean.addTags(pdto.getEditPostID(), pdto.getNewTags());

        if (editedPost != null) {
            log.info("Added tags: Tags " + tagArrayToString(pdto.getNewTags()) + " to post " + editedPost.getId());
        }
        else{
            log.info("Failed to add tags to post " + pdto.getEditPostID() + ": post does not exist");
        }

        return editedPost;
    }

    public Post removeTags(PostEditDTO pdto){
        Post editedPost = postBean.removeTags(pdto.getEditPostID(), pdto.getTagsToRemove());

        if (editedPost != null) {
            log.info("Removed tags: Tags " + tagArrayToString(pdto.getTagsToRemove()) + " for post " + editedPost.getId());
        }
        else{
            log.info("Failed to remove tags from post " + pdto.getEditPostID() + ": post does not exist");
        }

        return editedPost;
    }

    public Post editPostDescription(PostEditDTO pdto){
        Post editedPost = postBean.editDescription(pdto.getEditPostID(), pdto.getDescriptionEdit());

        if (editedPost != null) {
            log.info("Edited description of post: " + editedPost.getId());
        }
        else{
            log.info("Failed to edit description of post " + pdto.getEditPostID());
        }

        return editedPost;
    }

    public Post editPostScore(PostEditDTO pdto){
        Post editedPost = postBean.incrementScore(pdto.getEditPostID(), pdto.getScoreIncrement());

        if (editedPost != null) {
            log.info("Edited score of post: " + editedPost.getId() + " by " + pdto.getScoreIncrement());
        }
        else{
           log.info("Failed to edit score of post: " + pdto.getEditPostID() + ": post does not exist");
        }

        return editedPost;
    }

    public Post editPostRating(PostEditDTO pdto){
        if (validatePostRating(pdto.getNewRating())) {
            Post editedPost = postBean.editRating(pdto.getEditPostID(), pdto.getNewRating());

            if (editedPost != null){
                log.info("Edited rating on post " + editedPost.getId() + " to new rating '" + editedPost.getRating() + "'");
            }

            return editedPost;
        }
        else{
            log.info("Failed to edit rating of post " + pdto.getEditPostID() + ": invalid rating '" + pdto.getNewRating() + "'");
        }

        return null;
    }

    public Tag editTagDescription(TagEditDTO tdto){
        Tag editedTag = tagBean.editTagDescription(tdto.getId(), tdto.getDescription());

        if (editedTag != null){
            log.info("Edited description of tag '" + editedTag.getId() + "'");
        }
        else{
            log.info("Failed to edit description of tag '" + tdto.getId() + "': tag does not exist");
        }

        return editedTag;
    }

    public Tag editTagType(TagEditDTO tdto){
        if (validateTagType(tdto.getType())) {
            Tag editedTag = tagBean.editTagType(tdto.getId(), tdto.getType().toLowerCase());

            if (editedTag != null) {
                log.info("Edited type of tag '" + editedTag.getId() + "'");
            }
            else{
                log.info("Failed to edit type of tag '" + tdto.getId() + "': tag does not exist");
            }

            return editedTag;
        }
        else{
            log.info("Failed to edit type of tag '" + tdto.getId() + "': invalid tag type '" + tdto.getType() + "'");
        }

        return  null;
    }

    // Tag type can only be one of four acceptable
    private boolean validateTagType(String tagType){
        String[] validTypes = new String[]{"artist", "copyright", "species", "general"};
        boolean valid = Arrays.stream(validTypes).anyMatch(tagType.toLowerCase()::equals);

        if (!valid){
            log.info("Invalid tag type: " + tagType);
        }

        return valid;
    }

    // Post rating can only be one of three acceptable
    private boolean validatePostRating(String rating){
        String[] validTypes = new String[]{"safe", "questionable", "explicit"};
        boolean valid = Arrays.stream(validTypes).anyMatch(rating.toLowerCase()::equals);

        if (!valid){
            log.info("Invalid post rating: " + rating);
        }

        return valid;
    }

    private String tagSetToString(Set<Tag> tags){
        String listString = "";
        for (Tag tag : tags){
            listString +=  tag.getId() + ", ";
        }
        listString = "[" + listString.substring(0, listString.length()-2) + "]";

        return listString;
    }

    private String tagArrayToString(String[] tags){
        String listString = "";
        for (String tagName : tags){
            listString +=  tagName + ", ";
        }
        listString = "[" + listString.substring(0, listString.length()-2) + "]";

        return listString;
    }

}
