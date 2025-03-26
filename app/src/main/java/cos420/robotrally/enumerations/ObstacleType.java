package cos420.robotrally.enumerations;

import java.security.InvalidParameterException;

/**
 * Enum for the different types of obstacles
 */
public enum ObstacleType {
    CHAIR,
    WALL;

    public static ObstacleType getTypeFromString(String type)
    {
        switch(type.toLowerCase().trim())
        {
            case "chair": return CHAIR;
            case "wall": return WALL;
        }

        throw new InvalidParameterException("type does not match an ObstacleType");
    }
}
