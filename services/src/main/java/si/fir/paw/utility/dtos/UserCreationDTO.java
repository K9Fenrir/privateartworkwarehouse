package si.fir.paw.utility.dtos;

import java.io.Serializable;

public class UserCreationDTO implements Serializable {

    private String username;

    private String email;

    public UserCreationDTO(){
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
}
