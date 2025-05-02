package cos420.robotrally.enumerations;

/**
 * Enum to track if a command is in a subroutine
 */
public enum SubroutineType {
    /** Part of subroutine A */
    A,
    /** Part of subroutine B */
    B,
    /** Part of subroutine B, but nested in subroutine A */
    AB,
}
