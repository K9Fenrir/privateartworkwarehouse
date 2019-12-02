package si.fir.paw.utility.dtos.read;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

    // General data

    private int userID;

    private String username;

    private String email;

    private String password;

    private boolean admin;


    // Return-specific data

    private List<PostDTO> uploads;

    private List<PostDTO> favourites;


    // Constructor

    public UserDTO(){
        // O hai!
    }


    // Getters & Setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<PostDTO> getUploads() {
        return uploads;
    }

    public void setUploads(List<PostDTO> uploads) {
        this.uploads = uploads;
    }

    public List<PostDTO> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<PostDTO> favourites) {
        this.favourites = favourites;
    }
}
