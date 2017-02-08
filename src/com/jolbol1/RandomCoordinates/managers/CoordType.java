package com.jolbol1.RandomCoordinates.managers;

/**
 * Created by James on 05/07/2016.
 * These Enums enable me to see in what way they initiated the Random Teleport.
 * This helps to set custom prices etc based on each command withinh the command.
 */
public enum CoordType {
    /**
     * This is used when they are teleported by the command /RC
     */
    COMMAND,
    /**
     * This is used when they are teleported by the command /RC All
     */
    ALL,
    /**
     * This is used when they are teleported by /RC Player {Name}
     */
    PLAYER,
    /**
     * This is used when they are teleported by a [RandomCoords] sign
     */
    SIGN,
    /**
     * This is used when they are teleported on join
     */
    JOIN,
    /**
     * This is used when they are teleported by using a portal
     */
    PORTAL,
    /**
     * This is used when they are teleported by using /RC Warp
     */
    WARPS,
    /**
     * Used for custom world teleport via /RC Warp
     */
    WARPWORLD,

    JOINWORLD,

}
