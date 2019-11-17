package si.fri.paw.enums;

public class PAW_Enums{

    public enum TagType {
        artist,
        species,
        general;

        public static TagType getTagType(String tagType){
            switch(tagType){
                case "artist":
                    return TagType.artist;
                case "species":
                    return TagType.species;
                case "general":
                    return TagType.general;
                default:
                    return null;
            }
        }
    }
}

