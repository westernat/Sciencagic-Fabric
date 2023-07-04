package org.mesdag.scma.util;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class Properties {
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final IntProperty COLOR = IntProperty.of("color", 0, 4);
    public static final BooleanProperty ON_USE = BooleanProperty.of("on_use");
}
