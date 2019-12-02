package si.fri.paw.entities;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "post")
@NamedQueries(value =
        {
                @NamedQuery(name = "Post.getAll", query = "SELECT p FROM post p")
        })
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private String rating;

    @Column(columnDefinition = "integer default 0")
    private int score;

    @ManyToMany
    private Set<Tag> postTags;

    @ManyToMany
    private Set<User> favouritedBy;

    @ManyToOne
    private User author;

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

}
