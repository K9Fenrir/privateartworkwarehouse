package si.fir.paw.utility.mappers;

import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;

import java.util.LinkedList;
import java.util.List;

public class TagMapper {

    public static TagDTO minimalTagToDTO(Tag tag){

        TagDTO tdto = new TagDTO();

        tdto.setName(tag.getId());
        tdto.setType(tag.getType());
        tdto.setNoTaggedPosts(tag.getTaggedPosts().size());

        return tdto;
    }

    public static TagDTO tagToDTO(Tag tag){

        TagDTO tdto = new TagDTO();

        List<PostDTO> taggedPostsList = new LinkedList<>();

        for (Post post : tag.getTaggedPosts()){
            taggedPostsList.add(PostMapper.minimalPostToDto(post));
        }

        tdto.setName(tag.getId());
        tdto.setDescription(tag.getDescription());
        tdto.setType(tag.getType());
        tdto.setNoTaggedPosts(tag.getTaggedPosts().size());
        tdto.setTaggedPosts(taggedPostsList);

        return tdto;
    }

}
