package si.fir.paw.utility.beans;

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

    private static final Logger log = Logger.getLogger(CreateBean.class.getName());

    public PostDTO createNewPost(PostCreateDTO pdto) throws IOException{
        if (validatePostRating(pdto.getRating())) {

            log.info("Validated rating.");

            Post newPost = postBean.addPost(pdto.getTagNames(), pdto.getDescription(), pdto.getRating().toLowerCase(), pdto.getAuthorID());

            log.info("DB entry created.");
            if (pdto.getFileInputStream() != null){

                saveImage(pdto.getFileInputStream(), newPost.getId(), pdto.getFileExtension());
            }

            return PostMapper.postToDTO(newPost);
        }

        return null;
    }

    public UserDTO createNewUser(UserCreateDTO udto){

        if (validateEmail(udto.getEmail()) && validateUsername(udto.getUsername())){
            User newUser = userBean.addNewUser(udto.getUsername(), udto.getEmail());

            log.info("New user was added: " + newUser.getUsername());

            return UserMapper.userToDTO(newUser);
        }
        else{
            log.info("User creation failed: invalid parameters");
        }

        return null;
    }

    public TagDTO createNewTag(TagCreateDTO tdto){
        if (validateTagName(tdto.getName()) && validateTagType(tdto.getType())){
            Tag newTag = tagBean.addNewTag(tdto.getName().toLowerCase(), tdto.getDescription(), tdto.getType().toLowerCase());

            log.info("New Tag was added: " + newTag.getId());

            return TagMapper.tagToDTO(newTag);
        }
        else{
            log.info("Tag creation failed: invalid parameters");
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
    private boolean validateEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        boolean valid = matcher.matches();
        if (!valid){
            log.info("Invalid email: " + email);
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
            log.info("Invalid username: " + username);
        }

        return valid;
    }

    // Tag name can only contain lower & uppercase letters, digits from 0 to 9, underscores, and dashes
    private boolean validateTagName(String tagName){
        String regex = "^[a-zA-Z0-9_/-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tagName);

        boolean valid = matcher.matches();
        if (!valid){
            log.info("Invalid tag name: " + tagName);
        }

        return valid;
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

}
