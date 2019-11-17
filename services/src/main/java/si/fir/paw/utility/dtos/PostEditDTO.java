package si.fir.paw.utility.dtos;

import java.io.Serializable;

public class PostEditDTO implements Serializable {

    private int EditPostID;

    private int favouriteEditUserID;

    private String descriptionEdit;

    private String[] newTags;

    private String[] tagsToRemove;

    public PostEditDTO(){
    }

    public int getEditPostID() {
        return EditPostID;
    }

    public void setEditPostID(int favouriteEditPostID) {
        this.EditPostID = favouriteEditPostID;
    }

    public int getFavouriteEditUserID() {
        return favouriteEditUserID;
    }

    public void setFavouriteEditUserID(int favouriteEditUserID) {
        this.favouriteEditUserID = favouriteEditUserID;
    }

    public String getDescriptionEdit() {
        return descriptionEdit;
    }

    public void setDescriptionEdit(String descriptionEdit) {
        this.descriptionEdit = descriptionEdit;
    }

    public String[] getNewTags() {
        return newTags;
    }

    public void setNewTags(String[] newTags) {
        this.newTags = newTags;
    }

    public String[] getTagsToRemove() {
        return tagsToRemove;
    }

    public void setTagsToRemove(String[] tagsToRemove) {
        this.tagsToRemove = tagsToRemove;
    }
}
