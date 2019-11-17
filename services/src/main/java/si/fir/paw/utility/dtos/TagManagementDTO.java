package si.fir.paw.utility.dtos;

import si.fri.paw.entities.Post;

import java.io.Serializable;
import java.util.Set;

public class TagManagementDTO implements Serializable {

    private String id;

    private String description;

    private String type;

    private Set<Post> taggedPosts;

    public TagManagementDTO(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<Post> getTaggedPosts() {
        return taggedPosts;
    }

    public void setTaggedPosts(Set<Post> taggedPosts) {
        this.taggedPosts = taggedPosts;
    }
}
