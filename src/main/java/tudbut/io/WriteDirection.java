package tudbut.io;

/**
 * Indicator for how to write/read in TypedOutputStream and TypedInputStream
 */
public enum WriteDirection {
    
    /**
     * Integer 1 writes as
     * 0x00 0x00 0x00 0x01 -
     *
     * Integer 256 writes as
     * 0x00 0x00 0x01 0x00 <br/>
     * <br/>
     * This is the standard for most java applications
     */
    HIGH_FIRST,
    /**
     * Integer 1 writes as
     * 0x01 0x00 0x00 0x00 -
     *
     * Integer 256 writes as
     * 0x00 0x01 0x00 0x00 <br/>
     * <br/>
     * This is most commonly used in C/C++
     */
    LOW_FIRST,
    ;
}
