package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostEditDTO;
import si.fir.paw.utility.dtos.TagEditDTO;
import si.fir.paw.utility.dtos.UserEditDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public User editUserUsername(UserEditDTO udto){
        if (validateUsername(udto.getNewUsername())){
            User editedUser = userBean.editUserUsername(udto.getId(), udto.getNewUsername());

            if (editedUser != null){
                log.info("Edited username of user " + editedUser.getId() + "to '" + editedUser.getUsername() + "'");
            }
            else{
                log.info("Failed to edit username of user " + udto.getId() + ": user does not exist");
            }

            return editedUser;
        }
        else{
            log.info("Failed to edit username of user " + udto.getId() + ": invalid new username");
        }

        return null;
    }

    public User editUserEmail(UserEditDTO udto){
        if (validateEmail(udto.getNewEmail())){
            User editedUser = userBean.editUserEmail(udto.getId(), udto.getNewEmail());

            if (editedUser != null){
                log.info("Edited email of user " + editedUser.getId() + "to '" + editedUser.getEmail() + "'");
            }
            else{
                log.info("Failed to edit email of user " + udto.getId() + ": user does not exist");
            }

            return editedUser;
        }
        else{
            log.info("Failed to edit email of user " + udto.getId() + ": invalid new email");
        }

        return null;
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

    // Email has to be a valid email address
    private boolean validateEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        boolean valid = matcher.matches();
        if (!valid){
            log.info("Invalid edit email: " + email);
        }

        return valid;
    }

    // Username can only contain lower & uppercase letters, digits from 0 to 9, underscores, dots, and dashes
    private boolean validateUsername(String username){
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        boolean valid = matcher.matches();
        if (!valid){
            log.info("Invalid edit username: " + username);
        }

        return valid;
    }

}
