package cos420.robotrally.enumerations;

import java.security.InvalidParameterException;

/**
 * Enum for the different types of special commands
 */
public enum SpecialCommandType {
    D3,
    R3,
    L2; // need to have semicolon after last command

    /**
     * Convert a string version of enum to the actual enum
     * @param type the string version of the enum
     * @return SpecialCommandType that corresponds to the provided type string
     * @throws InvalidParameterException Throws exception if there is no matching SpecialCommandType
     */
    public static SpecialCommandType getTypeFromString(String type) throws InvalidParameterException
    {
        switch(type.toLowerCase().trim())
        {
            case "d3": return D3;
            case "r3": return R3;
            case "l2": return L2;
        }

        throw new InvalidParameterException("type does not match a SpecialCommandType");
    }
}
