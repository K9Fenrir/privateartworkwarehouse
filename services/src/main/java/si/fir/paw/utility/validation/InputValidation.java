package si.fir.paw.utility.validation;

import si.fir.paw.utility.Exceptions.InvalidParameterException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    // Email has to be a valid email address
    public static boolean validateEmail(String email) throws InvalidParameterException {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + email + "' is not a valid e-mail address");
        }

        return true;
    }

    // Username can only contain lower & uppercase letters, digits from 0 to 9, underscores, dots, and dashes
    public static boolean validateUsername(String username) throws InvalidParameterException{
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + username + "' is not a valid username");
        }

        return true;
    }

    // Tag name can only contain lower & uppercase letters, digits from 0 to 9, underscores, and dashes
    public static boolean validateTagName(String tagName) throws InvalidParameterException{
        String regex = "^[a-zA-Z0-9_/-]{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tagName);

        if (!matcher.matches()){
            throw new InvalidParameterException("'" + tagName + "' is not a valid tag name");
        }

        return true;
    }

    // Tag type can only be one of four acceptable
    public static boolean validateTagType(String tagType) throws InvalidParameterException{
        String[] validTypes = new String[]{"artist", "character", "copyright", "species", "general"};

        if (!Arrays.stream(validTypes).anyMatch(tagType.toLowerCase()::equals)){
            throw new InvalidParameterException("'" + tagType + "' is not a valid tag type");
        }

        return true;
    }

    // Post rating can only be one of three acceptable
    public static boolean validatePostRating(String rating) throws InvalidParameterException{
        String[] validRatings = new String[]{"safe", "questionable", "explicit"};

        if (!Arrays.stream(validRatings).anyMatch(rating.toLowerCase()::equals)){
            throw new InvalidParameterException("'" + rating + "' is not a valid post rating");
        }

        return true;
    }

}
