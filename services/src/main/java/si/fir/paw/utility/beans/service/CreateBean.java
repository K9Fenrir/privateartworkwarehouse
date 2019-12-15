package si.fir.paw.utility.beans.service;

import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.entity.PostBean;
import si.fir.paw.utility.beans.entity.TagBean;
import si.fir.paw.utility.beans.entity.UserBean;
import si.fir.paw.utility.beans.security.AuthenticationBean;
import si.fir.paw.utility.dtos.create.PostCreateDTO;
import si.fir.paw.utility.dtos.create.TagCreateDTO;
import si.fir.paw.utility.dtos.create.UserCreateDTO;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.util.logging.Logger;

@ApplicationScoped
public class CreateBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    @Inject
    AuthenticationBean authenticationBean;

    @Context
    protected UriInfo uriInfo;

    private static final Logger log = Logger.getLogger(CreateBean.class.getName());

    public PostDTO createNewPost(PostCreateDTO pdto) throws InvalidParameterException {
        if (InputValidation.validatePostRating(pdto.getRating())) {

            Post newPost = postBean.addPost(pdto.getTagNames(), pdto.getDescription(), pdto.getRating().toLowerCase(), pdto.getAuthorID());

            if (pdto.getFileInputStream() != null){

                saveImage(pdto.getFileInputStream(), newPost.getId(), pdto.getFileExtension());
            }

            return PostMapper.postToDTO(newPost);
        }

        return null;
    }

    public UserDTO createNewUser(UserCreateDTO udto) throws InvalidParameterException, PersistenceException {

        if (InputValidation.validateEmail(udto.getEmail()) && InputValidation.validateUsername(udto.getUsername())){
            String passHash = authenticationBean.encode(udto.getPassword());

            User newUser = userBean.addNewUser(udto.getUsername(), udto.getEmail(), passHash);

            log.info("New user was added: '" + newUser.getUsername() + "'");

            return UserMapper.userToDTO(newUser);
        }

        return null;
    }

    public TagDTO createNewTag(TagCreateDTO tdto) throws InvalidParameterException, PersistenceException {
        if (InputValidation.validateTagName(tdto.getName()) && InputValidation.validateTagType(tdto.getType())){
            Tag newTag = tagBean.addNewTag(tdto.getName().toLowerCase(), tdto.getDescription(), tdto.getType().toLowerCase());

            log.info("New Tag was added: " + newTag.getId());

            return TagMapper.tagToDTO(newTag);
        }

        return null;
    }

    // Saves uploaded image as .jpg in /images
    private void saveImage(InputStream fileInputStream, int postID, String fileExtension) throws InvalidParameterException{
        try {

            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(new File(System.getProperty("user.dir") + "/images/" + postID + "." + fileExtension));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        }
        catch (IOException e){
            throw new InvalidParameterException("Please upload a valid file.");
        }
    }
}
