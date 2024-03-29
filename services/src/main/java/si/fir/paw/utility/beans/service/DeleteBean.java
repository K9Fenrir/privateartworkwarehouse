package si.fir.paw.utility.beans.service;

import si.fir.paw.utility.beans.entity.PostBean;
import si.fir.paw.utility.beans.entity.TagBean;
import si.fir.paw.utility.beans.entity.UserBean;
import si.fir.paw.utility.dtos.delete.PostDeleteDTO;
import si.fir.paw.utility.dtos.delete.TagDeleteDTO;
import si.fir.paw.utility.dtos.delete.UserDeleteDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
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

    public void deleteUser(UserDeleteDTO udto) throws PersistenceException {
        userBean.deleteUser(udto.getUsername());
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
