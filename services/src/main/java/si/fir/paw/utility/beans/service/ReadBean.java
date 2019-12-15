package si.fir.paw.utility.beans.service;

import si.fir.paw.utility.beans.entity.PostBean;
import si.fir.paw.utility.beans.entity.TagBean;
import si.fir.paw.utility.beans.entity.UserBean;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fir.paw.utility.mappers.PostMapper;
import si.fir.paw.utility.mappers.TagMapper;
import si.fir.paw.utility.mappers.UserMapper;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReadBean {

    @Inject
    PostBean postBean;

    @Inject
    TagBean tagBean;

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(ReadBean.class.getName());

    public List<Post> getAllPosts(){
        List<Post> posts = postBean.getAllPosts();

        return posts;
    }

    public PostDTO getPostById(int id) throws PersistenceException{
        Post post = postBean.getByID(id);

        if (post == null){
            throw new EntityNotFoundException("Post '" + id + "' could not be found");
        }

        return PostMapper.postToDTO(post);
    }

    public List<PostDTO> getPostsByTags(String[] tagNames) throws PersistenceException {

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

        List<PostDTO> postList = filteredPosts.stream().map(x -> PostMapper.minimalPostToDto(x)).collect(Collectors.toList());

        return postList;
    }

    public List<TagDTO> getAllTags(){
        List<Tag> tags = tagBean.getAllTags();
        List<TagDTO> tagDTOs = new LinkedList<>();

        for (Tag tag : tags){
            tagDTOs.add(TagMapper.minimalTagToDTO(tag));
        }

        return tagDTOs;
    }

    public TagDTO getTagByName(String name){
        Tag tag = tagBean.getById(name);

        return TagMapper.tagToDTO(tag);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userBean.getAllUsers();
        List<UserDTO> userDTOs = new LinkedList<>();

        for (User user : users) {
            userDTOs.add(UserMapper.userToDTO(user));
        }

        return userDTOs;
    }

    public UserDTO getUserByUsername(String username) throws PersistenceException{
        User user = userBean.getUserByUsername(username);

        if (user == null){
            throw new EntityNotFoundException("User '" + username + "' could not be found.");
        }

        return UserMapper.userToDTO(user);
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
