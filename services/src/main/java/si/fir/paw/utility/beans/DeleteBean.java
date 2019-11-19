package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostDeleteDTO;
import si.fir.paw.utility.dtos.TagDeleteDTO;
import si.fir.paw.utility.dtos.UserDeleteDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.logging.Logger;

@ApplicationScoped
public class DeleteBean {

    @Inject
    private TagBean tagBean;

    @Inject
    private PostBean postBean;

    @Inject
    private UserBean userBean;

    private static final Logger log = Logger.getLogger(TagBean.class.getName());

    public boolean deleteTag(TagDeleteDTO tdto){
        boolean success = tagBean.removeTag(tdto.getId());

        if (success){
            log.info("Deleted tag '" + tdto.getId() + "'");
        }
        else{
            log.info("Failed to delete tag '" + tdto.getId() + "'");
        }

        return success;
    }

    public boolean deletePost(PostDeleteDTO pdto){
        boolean success = postBean.removePost(pdto.getPostID());
        deleteFile(pdto.getPostID());

        if (success){
            log.info("Deleted post " + pdto.getPostID());
        }
        else{
            log.info("Failed to delete post " + pdto.getPostID());
        }

        return success;
    }

    public boolean deleteUser(UserDeleteDTO udto){
        boolean success = userBean.removeUser(udto.getToDeleteID());

        if (success){
            log.info("Deleted user " + udto.getToDeleteID());
        }
        else{
            log.info("Failed to delete user " + udto.getToDeleteID());
        }

        return success;
    }

    private void deleteFile(int imageID){
        try {
            File f = new File(System.getProperty("user.dir") + "/images" + File.separator
                    + imageID + ".jpg");
            if (f.delete()) {
                log.info("Deleted image " + imageID + ".jpg");
            } else {
                log.info("Failed to delete image " + imageID + ".jpg");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
