package si.fir.paw.utility.dtos;

import java.io.Serializable;

public class TagDeleteDTO implements Serializable {

    private String id;

    private int userID;

    public TagDeleteDTO(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
