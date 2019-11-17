package si.fri.paw.entities;

import si.fri.paw.enums.PAW_Enums;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "tag")
@NamedQueries(value =
        {
                @NamedQuery(name = "Tag.getAll", query = "SELECT t FROM tag t")
        })

public class Tag {

    @Id
    private String id;

    private String description;

    private String type;

    @ManyToMany(mappedBy = "postTags")
    private Set<Post> taggedPosts;

    public Tag() {
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
