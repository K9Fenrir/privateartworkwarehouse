package si.fir.paw.utility.dtos.delete;

import java.io.Serializable;

public class UserDeleteDTO implements Serializable {

    private String username;

    public UserDeleteDTO(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
