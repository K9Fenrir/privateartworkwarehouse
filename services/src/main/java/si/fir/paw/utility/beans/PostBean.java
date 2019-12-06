package si.fir.paw.utility.beans;

import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
//        em.flush();

        return newPost;

    }

    @Transactional
    public Post incrementScore(int postID, int inc){

        Post post = em.find(Post.class, postID);

        if (post != null) {
            post.setRating(post.getRating() + inc);

            em.merge(post);
        }

        return post;
    }

    @Transactional
    public Post editDescription(int postID, String description){
        Post post = em.find(Post.class, postID);

        if (post != null){
            post.setDescription(description);

            em.merge(post);
        }

        return post;
    }

    @Transactional
    public Post editRating(int postID, String rating){
        Post post = em.find(Post.class, postID);

        if (post != null){
            post.setRating(rating);

            em.merge(post);
        }

        return post;
    }

    @Transactional
    public Post addNewFavourite(int postID, int userID) {

        Post post = em.find(Post.class, postID);
        User user = em.find(User.class, userID);

        if (post != null){
            if (user != null){
                post.getFavouritedBy().add(user);
                user.getFavourites().add(post);

                em.merge(user);
            }

            em.merge(post);
        }

        return post;
    }

    @Transactional
    public Post removeFavourite(int postID, int userID){

        Post post = em.find(Post.class, postID);
        User user = em.find(User.class, userID);

        if (post != null){
            if (user != null){
                post.getFavouritedBy().remove(user);
                user.getFavourites().remove(post);

                em.merge(user);
            }

            em.merge(post);
        }

        return post;
    }

    @Transactional
    public Post addTags(int postID, String[] tagNames){

        Post post = em.find(Post.class, postID);

        if (post != null) {
            for (String tagName : tagNames) {
                Tag tag = em.find(Tag.class, tagName);

                if (tag != null) {
                    post.getPostTags().add(tag);
                    tag.getTaggedPosts().add(post);

                    em.merge(tag);
                }
            }

            em.merge(post);
            em.flush();
        }
        return post;
    }

    @Transactional
    public Post removeTags(int postID, String[] tagNames){

        Post post = em.find(Post.class, postID);

        if (post != null) {
            for (String tagName : tagNames) {
                Tag tag = em.find(Tag.class, tagName);

                if (post != null && tag != null) {
                    post.getPostTags().remove(tag);
                    tag.getTaggedPosts().remove(post);

                    em.merge(tag);
                }
            }

            em.merge(post);
            em.flush();
        }

        return post;
    }

    @Transactional
    public boolean removePost(int postID){

        Post post = em.find(Post.class, postID);

        if (post != null){
            for (Tag tag : post.getPostTags()){
                tag.getTaggedPosts().remove(post);
                em.merge(tag);
            }
            for (User user : post.getFavouritedBy()){
                user.getFavourites().remove(post);
                em.merge(user);
            }
            User author = post.getAuthor();
            author.getUploads().remove(post);
            em.merge(author);

            em.remove(post);

            return true;
        }

        return false;
    }

}
