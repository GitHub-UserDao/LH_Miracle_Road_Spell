package dev.lhkongyu.lhmiracleroadspell.tool;

import net.minecraft.world.phys.Vec3;

public class DirectionUtils {

    public enum Direction8 {

        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    public static Vec3 getMeteorTransmitDirection(Vec3 lookVec) {
        double angle = Math.atan2(lookVec.z, lookVec.x) * (180 / Math.PI);

        if (angle < 0) {
            angle += 360;
        }

        Direction8 fixedDirection = getClosestDirection(angle);

        switch (fixedDirection) {
            case NORTH:
                return new Vec3(-0.03473871946334839, 0, -0.525890052318573);
            case NORTHEAST:
                return new Vec3(0.3421366214752197, 0, -0.4008861482143402);
            case EAST:
                return new Vec3(0.5270183086395264, 0, -0.0043454417027533054);
            case SOUTHEAST:
                return new Vec3(0.37477290630340576, 0, 0.37055686116218567);
            case SOUTH:
                return new Vec3(0.0043454417027533054, 0, 0.5270183086395264);
            case SOUTHWEST:
                return new Vec3(-0.40152373909950256, 0, 0.33781397342681885);
            case WEST:
                return new Vec3(-0.5247442126274109, 0, 0.0029682775493711233);
            case NORTHWEST:
                return new Vec3(-0.41222813725471497, 0, -0.3247053921222687);
            default:
                return Vec3.ZERO; // 避免 null 返回值
        }
    }



    private static Direction8 getClosestDirection(double angle) {
        if (angle >= 337.5 || angle < 22.5) {
            return Direction8.EAST;
        } else if (angle >= 22.5 && angle < 67.5) {
            return Direction8.SOUTHEAST;
        } else if (angle >= 67.5 && angle < 112.5) {
            return Direction8.SOUTH;
        } else if (angle >= 112.5 && angle < 157.5) {
            return Direction8.SOUTHWEST;
        } else if (angle >= 157.5 && angle < 202.5) {
            return Direction8.WEST;
        } else if (angle >= 202.5 && angle < 247.5) {
            return Direction8.NORTHWEST;
        } else if (angle >= 247.5 && angle < 292.5) {
            return Direction8.NORTH;
        } else {
            return Direction8.NORTHEAST;
        }
    }
}
