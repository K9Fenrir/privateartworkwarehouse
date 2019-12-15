package si.fir.paw.utility.beans.entity;

import si.fri.paw.entities.Post;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

    public User getUserByUsername(String username){
        return em.find(User.class, username);
    }

    public List<User> getAllUsersCrit(){

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery();
        Root u = cq.from(User.class);
        Query query = em.createQuery(cq);

        log.info("UserBean UUID: " + uuid);

        return query.getResultList();
    }

    @Transactional
    public User addNewUser(String username, String email, String passHash) throws PersistenceException{

        if (em.find(User.class, username) != null){
            throw new EntityExistsException("User '" + username + "' already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassHash(passHash);
        newUser.setAdmin(false);

        em.persist(newUser);

        return newUser;
    }

    @Transactional
    public User updateUserEmail(String username, String email) throws PersistenceException{

        User user = em.find(User.class, username);

        if (user == null){
            throw new EntityNotFoundException("User '" + username + "' could not be found.");
        }

        user.setEmail(email);

        return user;
    }

    @Transactional
    public User updateUserAdminStatus(String username, boolean status) throws PersistenceException{

        User user = em.find(User.class, username);

        if (user == null){
            throw new EntityNotFoundException("User '" + username + "' could not be found.");
        }

        user.setAdmin(status);

        return user;
    }

    @Transactional
    public void deleteUser(String username) throws PersistenceException {

        User user = em.find(User.class, username);

        if (user == null) {
            throw new EntityNotFoundException("User '" + username + "' could not be found.");
        }

        for (Post post : user.getFavourites()){
            post.getFavouritedBy().remove(user);
        }
        for (Post post : user.getUploads()){
            post.setAuthor(null);
        }

        em.remove(user);
    }

}