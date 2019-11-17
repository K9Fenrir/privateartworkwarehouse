package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostEditDTO;
import si.fir.paw.utility.dtos.PostManagementDTO;
import si.fir.paw.utility.dtos.TagManagementDTO;
import si.fir.paw.utility.dtos.UserManagmentDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;
import si.fri.paw.enums.PAW_Enums;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class PostBean {

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    private static final Logger log = Logger.getLogger(TagBean.class.getName());

    public List<Post> getAllPosts() {
        return em.createNamedQuery("Post.getAll", Post.class).getResultList();
    }

    public Post getByID(int id){
        return em.find(Post.class, id);
    }

    @Transactional
    public Post addPost(String[] tagNames, String description, String rating, int authorID){

        Set<Tag> tags = new HashSet<>();

        for (String tagName : tagNames){
            Tag tag = em.find(Tag.class, tagName);
            if (tag != null){
//                log.info("Found tag: " + tag.getId());
                tags.add(tag);
            }
        }

        User author = em.find(User.class, authorID);

        Post newPost = new Post();
        newPost.setAuthor(author);
        newPost.setDescription(description);
        newPost.setFavouritedBy(new HashSet<>());
        newPost.setRating(rating);
        newPost.setPostTags(tags);

        // Add new post to tags and author
        for (Tag tag : tags){
            tag.getTaggedPosts().add(newPost);
        }
        author.getUploads().add(newPost);

        em.persist(newPost);
        em.flush();

        return newPost;

    }

    @Transactional
    public void incScore(int postID, int inc){

        Post post = em.find(Post.class, postID);

        post.setRating(post.getRating() + inc);

        em.merge(post);
    }

    @Transactional
    public void editDescription(int postID, String description){
        Post post = em.find(Post.class, postID);

        if (post != null){
            post.setDescription(description);
        }
    }

    @Transactional
    public void addNewFavourite(int postID, int userID) {

        Post post = em.find(Post.class, postID);

        if (post != null){
            User user = em.find(User.class, userID);
            if (user != null){
                post.getFavouritedBy().add(user);
                user.getFavourites().add(post);
            }
        }
    }

    @Transactional
    public void removeFavourite(int postID, int userID){

        Post post = em.find(Post.class, postID);

        if (post != null){
            User user = em.find(User.class, userID);
            if (user != null){
                post.getFavouritedBy().remove(user);
                user.getFavourites().remove(post);
            }
        }
    }

    @Transactional
    public void addTags(int postID, String[] tagNames){

        Post post = em.find(Post.class, postID);

        for (String tagName : tagNames){
            Tag tag = em.find(Tag.class, tagName);

            if (post != null && tag != null){
                post.getPostTags().add(tag);
                tag.getTaggedPosts().add(post);
            }
        }
    }

    @Transactional
    public void removeTags(int postID, String[] tagNames){

        Post post = em.find(Post.class, postID);
        for (String tagName : tagNames){
            Tag tag = em.find(Tag.class, tagName);

            if (post != null && tag != null){
                post.getPostTags().remove(tag);
                tag.getTaggedPosts().remove(post);
            }
        }
    }

    @Transactional
    public boolean removePost(PostManagementDTO pdto){

        Post post = em.find(Post.class, pdto.getId());

        if (post != null){


            em.remove(post);
        }
        else{
            return false;
        }

        return true;
    }

}
