package si.fir.paw.utility.dtos.create;

import javax.servlet.http.Part;
import java.io.File;
import java.io.Serializable;

public class PostCreateDTO implements Serializable {

    private String description;

    private String rating;

    private int authorID;

    private String[] tagNames;

    private File file;

    public PostCreateDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String[] getTagNames() {
        return tagNames;
    }

    public void setTagNames(String[] tagNames) {
        this.tagNames = tagNames;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
