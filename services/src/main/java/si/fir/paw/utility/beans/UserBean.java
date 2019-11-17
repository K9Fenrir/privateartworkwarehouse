package si.fir.paw.utility.beans;

import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserBean {

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    private static final Logger log = Logger.getLogger(UserBean.class.getName());

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

    public List<User> getUserByUsername(String username){

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root user = cq.from(User.class);
        ParameterExpression<String> un = cb.parameter(String.class);
        cq.select(user).where(cb.equal(user.get("username"), un));

        Query query = em.createQuery(cq);
        query.setParameter(un, username);

        return query.getResultList();
    }

    @Transactional
    public User addNewUser(String username, String email){

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);

        em.persist(newUser);
        em.flush();

        return newUser;
    }

    @Transactional
    public void editUser(String username, String email){

    }

    @Transactional
    public void deleteUser(int id){

        User user = em.find(User.class, id);

        if (user != null){
            log.info(user.getUsername());
            em.remove(user);
            em.flush();
        }
    }

}