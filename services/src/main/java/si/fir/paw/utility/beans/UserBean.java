package si.fir.paw.utility.beans;

import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class UserBean {

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    public List<User> getAllUsers() {
        return em.createNamedQuery("User.getAll").getResultList();
    }

    public List<User> getAllUsersCrit(){

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root u = cq.from(User.class);
        Query query = em.createQuery(cq);

        return query.getResultList();
    }
}