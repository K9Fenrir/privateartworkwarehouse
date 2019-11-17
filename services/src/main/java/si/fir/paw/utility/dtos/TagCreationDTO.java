package si.fir.paw.utility.dtos;

import java.io.Serializable;

public class TagCreationDTO implements Serializable {

    private String name;

    private String description;

    private String type;

    public TagCreationDTO(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
