package si.fir.paw.utility.dtos.create;

import java.io.Serializable;

public class UserCreateDTO implements Serializable {

    private String username;

    private String email;

    public UserCreateDTO(){
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
