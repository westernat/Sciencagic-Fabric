package org.mesdag.scma.util;

import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import org.mesdag.scma.registry.FluidRegistry;

import java.util.Map;

import static org.mesdag.scma.util.Properties.*;

public class Maps {
    public static final Map<Item, Integer> dyeMap = Map.of(
            Items.WHITE_DYE, 0,
            Items.RED_DYE, 1,
            Items.GREEN_DYE, 2,
            Items.BLUE_DYE, 3,
            Items.YELLOW_DYE, 4
    );
    public static final Map<Direction, BooleanProperty> dirMap = Map.of(
            Direction.DOWN, DOWN,
            Direction.UP, UP,
            Direction.NORTH, NORTH,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST,
            Direction.EAST, EAST
    );
    public static final Map<Integer, String> colorMap = Map.of(
            0, "WHITE",
            1, "RED",
            2, "GREEN",
            3, "BLUE",
            4, "YELLOW"
    );

    public static final Map<Item, Integer> burnableItems = createFuelTimeMap();

    public static Map<Item, Integer> createFuelTimeMap() {
        Map<Item, Integer> map = com.google.common.collect.Maps.newLinkedHashMap();
        addFuel(map, Items.LAVA_BUCKET, 20000);
        addFuel(map,  Blocks.COAL_BLOCK, 16000);
        addFuel(map, Items.BLAZE_ROD, 2400);
        addFuel(map, Items.COAL, 1600);
        addFuel(map, Items.CHARCOAL, 1600);
        addFuel(map,  ItemTags.LOGS, 300);
        addFuel(map, ItemTags.PLANKS, 300);
        addFuel(map, ItemTags.WOODEN_STAIRS, 300);
        addFuel(map, ItemTags.WOODEN_SLABS, 150);
        addFuel(map, ItemTags.WOODEN_TRAPDOORS, 300);
        addFuel(map, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        addFuel(map, Blocks.OAK_FENCE, 300);
        addFuel(map, Blocks.BIRCH_FENCE, 300);
        addFuel(map, Blocks.SPRUCE_FENCE, 300);
        addFuel(map, Blocks.JUNGLE_FENCE, 300);
        addFuel(map, Blocks.DARK_OAK_FENCE, 300);
        addFuel(map, Blocks.ACACIA_FENCE, 300);
        addFuel(map, Blocks.OAK_FENCE_GATE, 300);
        addFuel(map, Blocks.BIRCH_FENCE_GATE, 300);
        addFuel(map, Blocks.SPRUCE_FENCE_GATE, 300);
        addFuel(map, Blocks.JUNGLE_FENCE_GATE, 300);
        addFuel(map, Blocks.DARK_OAK_FENCE_GATE, 300);
        addFuel(map, Blocks.ACACIA_FENCE_GATE, 300);
        addFuel(map, Blocks.NOTE_BLOCK, 300);
        addFuel(map, Blocks.BOOKSHELF, 300);
        addFuel(map, Blocks.LECTERN, 300);
        addFuel(map, Blocks.JUKEBOX, 300);
        addFuel(map, Blocks.CHEST, 300);
        addFuel(map, Blocks.TRAPPED_CHEST, 300);
        addFuel(map, Blocks.CRAFTING_TABLE, 300);
        addFuel(map, Blocks.DAYLIGHT_DETECTOR, 300);
        addFuel(map, ItemTags.BANNERS, 300);
        addFuel(map, Items.BOW, 300);
        addFuel(map, Items.FISHING_ROD, 300);
        addFuel(map, Blocks.LADDER, 300);
        addFuel(map, ItemTags.SIGNS, 200);
        addFuel(map, Items.WOODEN_SHOVEL, 200);
        addFuel(map, Items.WOODEN_SWORD, 200);
        addFuel(map, Items.WOODEN_HOE, 200);
        addFuel(map, Items.WOODEN_AXE, 200);
        addFuel(map, Items.WOODEN_PICKAXE, 200);
        addFuel(map, ItemTags.WOODEN_DOORS, 200);
        addFuel(map, ItemTags.BOATS, 1200);
        addFuel(map, ItemTags.WOOL, 100);
        addFuel(map, ItemTags.WOODEN_BUTTONS, 100);
        addFuel(map, Items.STICK, 100);
        addFuel(map, ItemTags.SAPLINGS, 100);
        addFuel(map, Items.BOWL, 100);
        addFuel(map, ItemTags.CARPETS, 67);
        addFuel(map, Blocks.DRIED_KELP_BLOCK, 4001);
        addFuel(map, Items.CROSSBOW, 300);
        addFuel(map, Blocks.BAMBOO, 50);
        addFuel(map, Blocks.DEAD_BUSH, 100);
        addFuel(map, Blocks.SCAFFOLDING, 400);
        addFuel(map, Blocks.LOOM, 300);
        addFuel(map, Blocks.BARREL, 300);
        addFuel(map, Blocks.CARTOGRAPHY_TABLE, 300);
        addFuel(map, Blocks.FLETCHING_TABLE, 300);
        addFuel(map, Blocks.SMITHING_TABLE, 300);
        addFuel(map, Blocks.COMPOSTER, 300);
        addFuel(map, Blocks.AZALEA, 100);
        addFuel(map, Blocks.FLOWERING_AZALEA, 100);

        // 1单位燃料为200
        addFuel(map, FluidRegistry.petroleum_bucket, 8000);
        addFuel(map, FluidRegistry.naphtha_bucket, 32000);
        return map;
    }


    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        for (RegistryEntry<Item> itemRegistryEntry : Registry.ITEM.iterateEntries(tag)) {
            if (!isNonFlammableWood(itemRegistryEntry.value())) {
                fuelTimes.put(itemRegistryEntry.value(), fuelTime);
            }
        }

    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"));
            }
        } else {
            fuelTimes.put(item2, fuelTime);
        }
    }

}
