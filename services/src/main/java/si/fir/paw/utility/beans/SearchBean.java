package si.fir.paw.utility.beans;

import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class SearchBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(SearchBean.class.getName());

    public List<Post> getAllPosts(){
        List<Post> posts = postBean.getAllPosts();

        return posts;
    }

    public Post getPostByID(int id){
        Post post = postBean.getByID(id);

        return post;
    }

    public List<Post> getPostsByTags(String[] tagNames) throws PersistenceException {

        List<Tag> tagList = new LinkedList<>();
        for (String tagName : tagNames){
            try {
                // Find all specified tags
                Tag tag = tagBean.getById(tagName);
                if (tag != null){
//                    logAllPosts(tag);
                    tagList.add(tag);
                }
            }
            catch (PersistenceException e){
                log.info(e.getMessage());
            }
        }

        tagList = sortByNumberOfPosts(tagList);

        Set<Post> minimalPosts = tagList.get(0).getTaggedPosts();

        Set<Post> filteredPosts = filterByTags(tagNames, minimalPosts);

        for (Post post : filteredPosts){
            log.info("Post ID: " + post.getId());
        }

        List<Post> postList = new LinkedList<>();
        postList.addAll(filteredPosts);

        return postList;
    }

    public List<Tag> getAllTags(){
        List<Tag> tags = tagBean.getAllTags();

        return tags;
    }

    public Tag getTagByName(String name){
        Tag tag = tagBean.getById(name);

        return tag;
    }

    public List<User> getAllUsers(){
        List<User> users = userBean.getAllUsers();

        return users;
    }

    public List<User> getUserByUsername(String username){
        List<User> users = userBean.getUserByUsername(username);

        return users;
    }

    private Set<Post> filterByTags(String[] tagNames, Set<Post> posts){
        Set<Post> filteredPosts = new HashSet<>();

        boolean missingTag;
        boolean hasNeededTag;
        for (Post post : posts){
            log.info("Checking post: " + post.getId());
            missingTag = false;
            for (String neededTag : tagNames) {
                hasNeededTag = false;
                for (Tag tag : post.getPostTags()) {
                    if (tag.getId().equals(neededTag)){
                        hasNeededTag = true;
                        break;
                    }
                }
                if (!hasNeededTag){
                    missingTag = true;
                    break;
                }
            }
            if (!missingTag){
                filteredPosts.add(post);
            }
        }

        return filteredPosts;
    }

    private List<Tag> sortByNumberOfPosts(List<Tag> list){
        int i, j;
        Tag key;
        for (i = 1; i < list.size(); i++)
        {
            key = list.get(i);
            j = i - 1;

            while (j >= 0 && list.get(j).getTaggedPosts().size() > key.getTaggedPosts().size())
            {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
        return list;
    }

    private void logAllTagsOnPost(Post post){
        for (Tag tag : post.getPostTags()){
            log.info("Tag on post " + post.getId() + ": " + tag.getId());
        }
    }

    private void logAllPostsOnTag(Tag tag){
        for (Post post : tag.getTaggedPosts()){
            log.info("Post with tag '" + tag.getId() + "': " + post.getId());
        }
    }

}
