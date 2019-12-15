package si.fir.paw.utility.dtos.create;

import java.io.Serializable;

public class UserCreateDTO implements Serializable {

    private String username;

    private String email;

    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
