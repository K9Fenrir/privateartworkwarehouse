package si.fir.paw.utility.dtos.read;

import java.io.Serializable;
import java.util.List;

public class TagDTO implements Serializable {

    // General data

    private String name;

    private String description;

    private String type;


    // Read-specific data

    private int noTaggedPosts;

    private List<PostDTO> taggedPosts;


    // Constructor

    public TagDTO(){
        // O hai!
    }


    // Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNoTaggedPosts() {
        return noTaggedPosts;
    }

    public void setNoTaggedPosts(int noTaggedPosts) {
        this.noTaggedPosts = noTaggedPosts;
    }

    public List<PostDTO> getTaggedPosts() {
        return taggedPosts;
    }

    public void setTaggedPosts(List<PostDTO> taggedPosts) {
        this.taggedPosts = taggedPosts;
    }
}
