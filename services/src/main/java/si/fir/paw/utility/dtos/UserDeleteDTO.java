package si.fir.paw.utility.dtos;

import java.io.Serializable;

public class UserDeleteDTO implements Serializable {

    private int toDeleteID;

    private int userID;

    private String password;

    public UserDeleteDTO(){
    }

    public int getToDeleteID() {
        return toDeleteID;
    }

    public void setToDeleteID(int toDeleteID) {
        this.toDeleteID = toDeleteID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
