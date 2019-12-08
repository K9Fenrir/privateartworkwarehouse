package si.fir.paw.utility.dtos.read;

import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.List;

public class PostDTO implements Serializable {

    // General data

    private int id;

    private UserDTO author;

    private String description;

    private String rating;

    private int score;


    // Update/Create-specific data

    private List<String> tagNames;

    // Read-specific data

    private List<UserDTO> favourtedBy;

    private List<TagDTO> tags;


    // Constructor

    public PostDTO(){
        // O hai!
    }


    // Getters & Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public List<UserDTO> getFavourtedBy() {
        return favourtedBy;
    }

    public void setFavourtedBy(List<UserDTO> favourtedBy) {
        this.favourtedBy = favourtedBy;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
