package si.fir.paw.utility.mappers;

import org.json.JSONException;
import org.json.JSONObject;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.UserDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.User;

import java.util.LinkedList;
import java.util.List;

public class UserMapper {

    public static UserDTO userToDTO(User user){

        UserDTO udto = new UserDTO();
        List<PostDTO> uploadList = new LinkedList<>();
        List<PostDTO> favouriteList = new LinkedList<>();

        for (Post post : user.getUploads()){
            uploadList.add(PostMapper.minimalPostToDto(post));
        }
        for (Post post : user.getFavourites()){
            favouriteList.add(PostMapper.minimalPostToDto(post));
        }

        udto.setUserID(user.getId());
        udto.setUsername(user.getUsername());
        udto.setEmail(user.getEmail());
        udto.setAdmin(user.getAdmin());
        udto.setFavourites(favouriteList);
        udto.setUploads(uploadList);

        return udto;
    }

    public static UserDTO minimalUserToDTO(User user){

        UserDTO udto = new UserDTO();

        udto.setUserID(user.getId());
        udto.setUsername(user.getUsername());

        return udto;
    }

}
