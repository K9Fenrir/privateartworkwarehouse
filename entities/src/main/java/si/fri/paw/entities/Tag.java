package si.fri.paw.entities;

import javax.persistence.*;

@Entity(name = "tag")
@NamedQueries(value =
        {
                @NamedQuery(name = "Tag.getAll", query = "SELECT t FROM tag t")
        })

public class Tag {

    @Id
    private String id;

    private String description;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrption() {
        return description;
    }

    public void setDescrption(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
