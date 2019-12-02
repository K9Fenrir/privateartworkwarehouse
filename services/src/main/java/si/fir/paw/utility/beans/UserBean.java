package si.fir.paw.utility.beans;

import si.fri.paw.entities.Post;
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
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class UserBean {

    @PersistenceContext(unitName = "paw-jpa")
    private EntityManager em;

    private UUID uuid = UUID.randomUUID();

    private static final Logger log = Logger.getLogger(UserBean.class.getName());

    public List<User> getAllUsers() {
        return em.createNamedQuery("User.getAll").getResultList();
    }

    public User getUserByID(int id){
        return em.createNamedQuery("User.getByID", User.class).setParameter("id", id).getSingleResult();
    }

    public List<User> getAllUsersCrit(){

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root u = cq.from(User.class);
        Query query = em.createQuery(cq);

        log.info("UserBean UUID: " + uuid);

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
        newUser.setAdmin(false);

        em.persist(newUser);
        em.flush();

        return newUser;
    }

    @Transactional
    public User updateUserUsername(int id, String username){

        User user = em.find(User.class, id);

        if (user != null){
            user.setUsername(username);

            em.merge(user);
        }

        return user;
    }

    @Transactional
    public User updateUserEmail(int id, String email){

        User user = em.find(User.class, id);

        if (user != null){
            user.setEmail(email);

            user = em.merge(user);
        }

        return user;
    }

    @Transactional
    public User updateUserAdminStatus(int id, boolean status){

        User user = em.find(User.class, id);

        if (user != null){
            user.setAdmin(status);

            user = em.merge(user);
        }

        return user;
    }

    @Transactional
    public boolean removeUser(int id){

        User user = em.find(User.class, id);

        if (user != null){
            for (Post post : user.getFavourites()){
                post.getFavouritedBy().remove(user);
                em.merge(post);
            }
            for (Post post : user.getUploads()){
                post.setAuthor(null);
                em.merge(post);
            }

            em.remove(user);

            return true;
        }

        return false;
    }

}