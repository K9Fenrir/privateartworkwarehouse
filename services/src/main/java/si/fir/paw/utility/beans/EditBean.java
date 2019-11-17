package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostEditDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    public void newFavouritePost(PostEditDTO pdto){
        postBean.addNewFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        log.info("Added new favourite: Post " + pdto.getEditPostID() + " to User " + pdto.getFavouriteEditUserID());
    }

    public void removeFavouritePost(PostEditDTO pdto){
        postBean.removeFavourite(pdto.getEditPostID(), pdto.getFavouriteEditUserID());

        log.info("Removed favourite: Post " + pdto.getEditPostID() + " from User " + pdto.getFavouriteEditUserID());
    }

    public void addTags(PostEditDTO pdto){
        postBean.addTags(pdto.getEditPostID(), pdto.getNewTags());

        log.info("Added tags: Tags " + tagArrayToString(pdto.getNewTags()) + " to post " + pdto.getEditPostID());
    }

    public void removeTags(PostEditDTO pdto){
        postBean.removeTags(pdto.getEditPostID(), pdto.getTagsToRemove());

        log.info("Removed tags: Tags " + tagArrayToString(pdto.getTagsToRemove()) + " from post " + pdto.getEditPostID());
    }

    public void editDescription(PostEditDTO pdto){
        postBean.editDescription(pdto.getEditPostID(), pdto.getDescriptionEdit());

        log.info("Edited description of post: " + pdto.getEditPostID());
    }




    private String tagArrayToString(String[] tags){
        String listString = "";
        for (String tag : tags){
            listString +=  tag + ", ";
        }
        listString = "[" + listString.substring(0, listString.length()-2) + "]";

        return listString;
    }

}
