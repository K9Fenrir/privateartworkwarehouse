package si.fir.paw.utility.dtos;

import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import java.io.Serializable;
import java.util.Set;

public class PostManagementDTO implements Serializable {

    private int id;

    private String description;

    private String rating;

    private Set<Tag> postTags;

    private Set<User> favouritedBy;

    private User author;

    private Set<String> tagNames;

    public PostManagementDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Tag> getPostTags() {
        return postTags;
    }

    public void setPostTags(Set<Tag> postTags) {
        this.postTags = postTags;
    }

    public Set<User> getFavouritedBy() {
        return favouritedBy;
    }

    public void setFavouritedBy(Set<User> favouritedBy) {
        this.favouritedBy = favouritedBy;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }
}
