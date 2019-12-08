package si.fir.paw.utility.beans;

import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.dtos.create.PostCreateDTO;
import si.fir.paw.utility.dtos.create.TagCreateDTO;
import si.fir.paw.utility.dtos.create.UserCreateDTO;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fir.paw.utility.mappers.PostMapper;
import si.fir.paw.utility.mappers.TagMapper;
import si.fir.paw.utility.mappers.UserMapper;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.activation.MimeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CreateBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    @Context
    protected UriInfo uriInfo;

    private static final Logger log = Logger.getLogger(CreateBean.class.getName());

    public PostDTO createNewPost(PostCreateDTO pdto) throws IOException, InvalidParameterException{
        if (validatePostRating(pdto.getRating())) {

            Post newPost = postBean.addPost(pdto.getTagNames(), pdto.getDescription(), pdto.getRating().toLowerCase(), pdto.getAuthorID());

            if (pdto.getFileInputStream() != null){

                saveImage(pdto.getFileInputStream(), newPost.getId(), pdto.getFileExtension());
            }

            return PostMapper.postToDTO(newPost);
        }

        return null;
    }

    public UserDTO createNewUser(UserCreateDTO udto) throws InvalidParameterException {

        if (validateEmail(udto.getEmail()) && validateUsername(udto.getUsername())){
            User newUser = userBean.addNewUser(udto.getUsername(), udto.getEmail());

            log.info("New user was added: " + newUser.getUsername());

            return UserMapper.userToDTO(newUser);
        }

        return null;
    }

    public TagDTO createNewTag(TagCreateDTO tdto) throws InvalidParameterException{
        if (validateTagName(tdto.getName()) && validateTagType(tdto.getType())){
            Tag newTag = tagBean.addNewTag(tdto.getName().toLowerCase(), tdto.getDescription(), tdto.getType().toLowerCase());

            log.info("New Tag was added: " + newTag.getId());

            return TagMapper.tagToDTO(newTag);
        }

        return null;
    }

    // Saves uploaded image as .jpg in /images
    private void saveImage(InputStream fileInputStream, int postID, String fileExtension) throws IOException{
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
            throw new IOException("Shit's fucked, yo.");
        }
    }

    // Email has to be a valid email address
    private boolean validateEmail(String email) throws InvalidParameterException{
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + email + "' is not a valid e-mail address");
        }

        return true;
    }

    // Username can only contain lower & uppercase letters, digits from 0 to 9, underscores, dots, and dashes
    private boolean validateUsername(String username) throws InvalidParameterException{
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + username + "' is not a valid username");
        }

        return true;
    }

    // Tag name can only contain lower & uppercase letters, digits from 0 to 9, underscores, and dashes
    private boolean validateTagName(String tagName) throws InvalidParameterException{
        String regex = "^[a-zA-Z0-9_/-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tagName);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + tagName + "' is not a valid tag name");
        }

        return true;
    }

    // Tag type can only be one of four acceptable
    private boolean validateTagType(String tagType) throws InvalidParameterException{
        String[] validTypes = new String[]{"artist", "character", "copyright", "species", "general"};

        if (!Arrays.stream(validTypes).anyMatch(tagType.toLowerCase()::equals)){
            throw new InvalidParameterException("'" + tagType + "' is not a valid tag type");
        }

        return true;
    }

    // Post rating can only be one of three acceptable
    private boolean validatePostRating(String rating) throws InvalidParameterException{
        String[] validRatings = new String[]{"safe", "questionable", "explicit"};

        if (!Arrays.stream(validRatings).anyMatch(rating.toLowerCase()::equals)){
            throw new InvalidParameterException("'" + rating + "' is not a valid post rating");
        }

        return true;
    }

}
