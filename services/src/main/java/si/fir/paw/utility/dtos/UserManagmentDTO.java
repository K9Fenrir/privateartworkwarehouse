package si.fir.paw.utility.dtos;

import si.fri.paw.entities.Post;

import java.io.Serializable;
import java.util.Set;

public class UserManagmentDTO implements Serializable {

    private Integer id;

    private String username;

    private String email;

    private Set<Post> uploads;

    private Set<Post> favourites;

    public UserManagmentDTO(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Post> getUploads() {
        return uploads;
    }

    public void setUploads(Set<Post> uploads) {
        this.uploads = uploads;
    }

    public Set<Post> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<Post> favourites) {
        this.favourites = favourites;
    }
}
