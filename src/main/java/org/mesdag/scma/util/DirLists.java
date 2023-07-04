package org.mesdag.scma.util;

import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class DirLists {
    public static final ArrayList<Direction> emptyList = new ArrayList<>();
    public static final ArrayList<Direction> downList = new ArrayList<>(List.of(Direction.DOWN));
    public static final ArrayList<Direction> upList = new ArrayList<>(List.of(Direction.UP));
    public static final ArrayList<Direction> northList = new ArrayList<>(List.of(Direction.NORTH));
    public static final ArrayList<Direction> southList = new ArrayList<>(List.of(Direction.SOUTH));
    public static final ArrayList<Direction> eastList = new ArrayList<>(List.of(Direction.EAST));
    public static final ArrayList<Direction> westList = new ArrayList<>(List.of(Direction.WEST));
    public static final ArrayList<Direction> allList = new ArrayList<>(List.of(Direction.values()));
    public static final ArrayList<Direction> eastWestList = new ArrayList<>(List.of(Direction.WEST, Direction.EAST));
    public static final ArrayList<Direction> northSouthList = new ArrayList<>(List.of(Direction.NORTH, Direction.SOUTH));
    public static final ArrayList<Direction> northEastWestList = new ArrayList<>(List.of(Direction.NORTH, Direction.EAST, Direction.WEST));
    public static final ArrayList<Direction> northSouthEastList = new ArrayList<>(List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST));
    public static final ArrayList<Direction> northSouthWestList = new ArrayList<>(List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST));
    public static final ArrayList<Direction> southEastWestList = new ArrayList<>(List.of(Direction.SOUTH, Direction.EAST, Direction.WEST));
    public static final ArrayList<Direction> downUpEastList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.EAST));
    public static final ArrayList<Direction> downUpNorthList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.NORTH));
    public static final ArrayList<Direction> downUpSouthList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.SOUTH));
    public static final ArrayList<Direction> downUpWestList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.WEST));
    public static final ArrayList<Direction> exceptSouthList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.WEST));
    public static final ArrayList<Direction> exceptNorthList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.EAST, Direction.WEST));
    public static final ArrayList<Direction> exceptEastList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST));
    public static final ArrayList<Direction> exceptWestList = new ArrayList<>(List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST));
}
