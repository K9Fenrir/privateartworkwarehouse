package si.fir.paw.utility.beans.service;

import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.entity.PostBean;
import si.fir.paw.utility.beans.entity.TagBean;
import si.fir.paw.utility.beans.entity.UserBean;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.update.PostUpdateDTO;
import si.fir.paw.utility.dtos.update.TagUpdateDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fir.paw.utility.dtos.update.UserUpdateDTO;
import si.fir.paw.utility.mappers.PostMapper;
import si.fir.paw.utility.mappers.TagMapper;
import si.fir.paw.utility.mappers.UserMapper;
import si.fir.paw.utility.validation.InputValidation;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class UpdateBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(UpdateBean.class.getName());

    public PostDTO newFavouritePost(PostUpdateDTO pdto){
        Post editedPost = postBean.addNewFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        if (editedPost != null){
            log.info("Added new favourite: Post " + pdto.getEditPostID() + " to User " + pdto.getFavouriteEditUserID());

            return PostMapper.postToDTO(editedPost);
        }
        else{
            log.info("Failed to add favourite to post " + pdto.getEditPostID() + ": post or user does not exist");
        }

        return null;
    }

    public PostDTO removeFavouritePost(PostUpdateDTO pdto){
        Post editedPost = postBean.removeFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        if (editedPost != null) {
            log.info("Removed favourite: Post " + editedPost.getId() + " from User " + pdto.getFavouriteEditUserID());

            return PostMapper.postToDTO(editedPost);
        }
        else{
            log.info("Failed to remove favourite from post " + pdto.getEditPostID() + ": post or user does not exist");
        }

        return null;
    }

    public PostDTO addTags(PostUpdateDTO pdto){
        Post editedPost = postBean.addTags(pdto.getEditPostID(), pdto.getNewTags());

        if (editedPost != null) {
            log.info("Added tags: Tags " + tagArrayToString(pdto.getNewTags()) + " to post " + editedPost.getId());

            return PostMapper.postToDTO(editedPost);
        }
        else{
            log.info("Failed to add tags to post " + pdto.getEditPostID() + ": post does not exist");
        }

        return null;
    }

    public PostDTO removeTags(PostUpdateDTO pdto){
        Post editedPost = postBean.removeTags(pdto.getEditPostID(), pdto.getTagsToRemove());

        if (editedPost != null) {
            log.info("Removed tags: Tags " + tagArrayToString(pdto.getTagsToRemove()) + " for post " + editedPost.getId());

            return PostMapper.postToDTO(editedPost);
        }
        else{
            log.info("Failed to remove tags from post " + pdto.getEditPostID() + ": post does not exist");
        }

        return null;
    }

    public PostDTO updatePostDescription(PostUpdateDTO pdto){
        Post editedPost = postBean.editDescription(pdto.getEditPostID(), pdto.getDescriptionEdit());

        if (editedPost != null) {
            log.info("Edited description of post: " + editedPost.getId());
        }
        else{
            log.info("Failed to edit description of post " + pdto.getEditPostID());
        }

        return PostMapper.postToDTO(editedPost);
    }

    public PostDTO updatePostScore(PostUpdateDTO pdto){
        Post editedPost = postBean.incrementScore(pdto.getEditPostID(), pdto.getScoreIncrement());

        if (editedPost != null) {
            log.info("Edited score of post: " + editedPost.getId() + " by " + pdto.getScoreIncrement());
        }
        else{
           log.info("Failed to edit score of post: " + pdto.getEditPostID() + ": post does not exist");
        }

        return PostMapper.postToDTO(editedPost);
    }

    public PostDTO updatePostRating(PostUpdateDTO pdto) throws InvalidParameterException{
        if (InputValidation.validatePostRating(pdto.getNewRating())) {
            Post editedPost = postBean.editRating(pdto.getEditPostID(), pdto.getNewRating());

            if (editedPost != null){
                log.info("Edited rating on post " + editedPost.getId() + " to new rating '" + editedPost.getRating() + "'");
            }

            return PostMapper.postToDTO(editedPost);
        }
        else{
            log.info("Failed to edit rating of post " + pdto.getEditPostID() + ": invalid rating '" + pdto.getNewRating() + "'");
        }

        return null;
    }

    public TagDTO updateTag(TagUpdateDTO tdto) throws InvalidParameterException, PersistenceException{
        InputValidation.validateTagType(tdto.getType());

        Tag editedTag = tagBean.editTag(tdto.getId(), tdto.getDescription(), tdto.getType().toLowerCase());
        log.info("Edited tag '" + editedTag.getId() + "'");

        return TagMapper.tagToDTO(editedTag);
    }

    public UserDTO updateUserEmail(UserUpdateDTO udto) throws InvalidParameterException, PersistenceException{
        InputValidation.validateEmail(udto.getNewEmail());

        User editedUser = userBean.updateUserEmail(udto.getUsername(), udto.getNewEmail());
        log.info("Edited email of user " + editedUser.getUsername() + "to '" + editedUser.getEmail() + "'");

        return UserMapper.userToDTO(editedUser);
    }

    public UserDTO updateUserAdminStatus(UserUpdateDTO udto) throws PersistenceException{
        User editedUser = userBean.updateUserAdminStatus(udto.getUsername(), udto.isAdmin());
        log.info("Edited admin status of user " + editedUser.getUsername() + "to '" + udto.isAdmin() + "'");

        return UserMapper.userToDTO(editedUser);
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
