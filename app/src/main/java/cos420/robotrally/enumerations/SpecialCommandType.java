package cos420.robotrally.enumerations;

import java.security.InvalidParameterException;

/**
 * Enum for the different types of special commands
 */
public enum SpecialCommandType {
    REPEAT,
    OTHER;

    public static SpecialCommandType getTypeFromString(String type)
    {
        switch(type.toLowerCase().trim())
        {
            case "repeat": return REPEAT;
            case "other": return OTHER;
        }

        throw new InvalidParameterException("type does not match a SpecialCommandType");
    }
}
