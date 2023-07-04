package org.mesdag.scma.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.item.*;
import org.mesdag.scma.item.energy.BatteryItem;
import org.mesdag.scma.util.SCMAIdentifier;

import static org.mesdag.scma.SCMA.LOGGER;
import static org.mesdag.scma.registry.Groups.ITEMS;

public class ItemRegistry {
    public static final Item raw_moscovium = new Item(new Item.Settings().group(ITEMS).maxCount(16).fireproof());

    public static final Item puregic_ingot = new ElementIngot();

    public static final Pestle wooden_pestle = new Pestle(1, ToolMaterials.WOOD);
    public static final Pestle terracotta_pestle = new Pestle(2, ToolMaterials.STONE);
    public static final Pestle copper_pestle = new Pestle(3, ToolMaterials.IRON);


    public static final Item toner = new BaseItem();
    public static final Item mixtures = new BaseItem();



    public static void register(String id, Item item) {
        Registry.register(Registry.ITEM, new SCMAIdentifier(id), item);
    }

    public static void initialize() {
        register("plank", new BaseItem());
        register("stone_residue", new BaseItem());
        register("graphite", new BaseItem());
        register("silicon", new BaseItem());
        register("toughened_glass", new BaseItem());
        register("sulfur", new BaseItem());
        register("phase_fabric", new BaseItem());
        register("raw_moscovium", raw_moscovium);
        register("lead_ingot", new BaseItem());
        register("silver_ingot", new BaseItem());
        register("titanium_ingot", new BaseItem());
        register("bedrock_ingot", new Item(new Item.Settings().group(ITEMS).maxCount(32).fireproof().rarity(Rarity.EPIC)));
        register("moscovium_ingot", new Item(new Item.Settings().group(ITEMS).fireproof()));
        register("resin", new BaseItem());

        register("puregic_ingot", puregic_ingot);
        register("meta_eart_alloy", new ElementIngot());
        register("fore_torn_alloy", new ElementIngot());
        register("dimn_mari_alloy", new ElementIngot());
        register("flam_brig_alloy", new ElementIngot());
        register("froz_thun_alloy", new ElementIngot());
        register("time_spac_alloy", new ElementIngot());

        register("red_soul", new RedSoul());

        register("magical_book", new MagicalBook());

        register("wrench", new Wrench());
        register("wooden_pestle", wooden_pestle);
        register("terracotta_pestle", terracotta_pestle);
        register("copper_pestle", copper_pestle);

        register("toner", toner);
        register("mixtures", mixtures);

        register("small_battery", new BatteryItem(20000, 100));

        LOGGER.info("Items loaded");
    }
}
