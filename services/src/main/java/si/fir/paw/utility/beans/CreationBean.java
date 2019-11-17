package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.*;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CreationBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(CreationBean.class.getName());

    public Post createNewPost(PostCreationDTO pdto) throws IOException{
        if (validatePostRating(pdto.getRating())) {
            Post newPost = postBean.addPost(pdto.getTagNames(), pdto.getDescription(), pdto.getRating().toLowerCase(), pdto.getAuthorID());

            if (pdto.getFilePart() != null){
                saveImage(pdto.getFilePart(), newPost.getId());
            }

            return newPost;
        }

        return null;
    }

    public User createNewUser(UserCreationDTO udto){
        User newUser = null;
        if (validateEmail(udto.getEmail()) && validateUsername(udto.getUsername())){
            newUser = userBean.addNewUser(udto.getUsername(), udto.getEmail());

            log.info("New user was added: " + newUser.getUsername());
        }
        else{
            log.info("User creation failed: invalid parameters");
        }

        return newUser;
    }

    public Tag createNewTag(TagCreationDTO tdto){
        Tag newTag = null;
        if (validateTagName(tdto.getName()) && validateTagType(tdto.getType())){
            newTag = tagBean.addNewTag(tdto.getName().toLowerCase(), tdto.getDescription(), tdto.getType().toLowerCase());

            log.info("New Tag was added: " + newTag.getId());
        }
        else{
            log.info("Tag creation failed: invalid parameters");
        }

        return newTag;
    }

    // Saves uploaded image as .jpg in /images
    private void saveImage(Part filePart, int postID) throws IOException{

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(System.getProperty("user.dir") + "/images" + File.separator
                    + postID + ".jpg"));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            log.info("New file created at /images/" + postID + ".jpg");

        } catch (FileNotFoundException fne) {
            log.info("You did not specify a file to upload.");

        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
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
