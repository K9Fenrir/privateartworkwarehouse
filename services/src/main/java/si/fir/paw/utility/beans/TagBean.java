package si.fir.paw.utility.beans;

import si.fri.paw.entities.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class TagBean {

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    public List<Tag> getUsers() {
        return em.createNamedQuery("Tag.getAll", Tag.class).getResultList();
    }

}
