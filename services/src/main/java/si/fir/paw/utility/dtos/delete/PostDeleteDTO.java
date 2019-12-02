package si.fir.paw.utility.dtos.delete;

import java.io.Serializable;

public class PostDeleteDTO implements Serializable {

    private int userID;

    private int postID;

    public PostDeleteDTO(){
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
}
