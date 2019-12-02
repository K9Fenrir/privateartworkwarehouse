package si.fir.paw.utility.dtos.create;

import java.io.Serializable;

public class TagCreateDTO implements Serializable {

    private String name;

    private String description;

    private String type;

    public TagCreateDTO(){
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
