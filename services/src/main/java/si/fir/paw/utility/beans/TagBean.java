package si.fir.paw.utility.beans;

import si.fir.paw.utility.dtos.PostManagementDTO;
import si.fir.paw.utility.dtos.TagManagementDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TagBean {

    @Inject
    private PostBean postBean;

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    private static final Logger log = Logger.getLogger(TagBean.class.getName());

    public List<Tag> getAllTags() {
        return em.createNamedQuery("Tag.getAll", Tag.class).getResultList();
    }

    public Tag getById(String id){
        return em.find(Tag.class, id);
    }

    @Transactional
    public Tag addNewTag(String id, String description, String type) {

        Tag newTag = new Tag();
        newTag.setId(id);
        newTag.setDescription(description);
        newTag.setType(type);

        em.persist(newTag);
        em.flush();

        return newTag;
    }

    @Transactional
    public Tag editTag(String id, String description, String type){

        Tag tag = em.find(Tag.class, id);

        if (tag != null){
            if (description != null){
                tag.setDescription(description);
            }
            if (type != null){
                tag.setType(type);
            }
        }

        return tag;

    }

    @Transactional
    public void removeTag(String id){

        Tag tag = em.find(Tag.class, id);

        if (tag != null){
            PostManagementDTO pdto = new PostManagementDTO();
            TagManagementDTO tdto = new TagManagementDTO();
            tdto.setId(tag.getId());
            for (Post post : tag.getTaggedPosts()){
                pdto.setId(post.getId());
            }
            em.remove(tag);
        }
    }
}
