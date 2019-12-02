package si.fir.paw.utility.mappers;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import java.util.LinkedList;
import java.util.List;

public class PostMapper {

    public static PostDTO postToDTO(Post post){

        PostDTO pdto = new PostDTO();
        if (post == null) {
            return pdto;
        }

            List<TagDTO> tags = new LinkedList<>();
        List<UserDTO> favouritedBy = new LinkedList<>();

        for (Tag tag : post.getPostTags()){
            tags.add(TagMapper.minimalTagToDTO(tag));
        }
        for (User user : post.getFavouritedBy()){
            favouritedBy.add(UserMapper.minimalUserToDTO(user));
        }

        pdto.setId(post.getId());
        pdto.setAuthor(UserMapper.minimalUserToDTO(post.getAuthor()));
        pdto.setDescription(post.getDescription());
        pdto.setRating(post.getRating());
        pdto.setScore(post.getScore());
        pdto.setTags(tags);
        pdto.setFavourtedBy(favouritedBy);

        return pdto;
    }

    public static PostDTO minimalPostToDto(Post post){

        PostDTO pdto = new PostDTO();
        if (post == null){
            return pdto;
        }

        List<String> tagNames = new LinkedList<>();

        for (Tag tag : post.getPostTags()){
            tagNames.add(tag.getId());
        }

        pdto.setId(post.getId());
        pdto.setRating(post.getRating());
        pdto.setScore(post.getScore());
        pdto.setTagNames(tagNames);

        return pdto;
    }

}
