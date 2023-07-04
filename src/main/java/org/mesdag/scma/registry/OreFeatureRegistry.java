package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.mesdag.scma.util.SCMAIdentifier;

import java.util.List;

import static org.mesdag.scma.SCMA.LOGGER;

public class OreFeatureRegistry {
    private static void register(String id, Block block, RuleTest rule, int size, int count, YOffset minY, YOffset maxY) {
        ConfiguredFeature<?, ?> configured = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(rule, block.getDefaultState(), size));
        PlacedFeature placed = new PlacedFeature(RegistryEntry.of(configured), List.of(CountPlacementModifier.of(count), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(minY, maxY)));
        Identifier identifier = new SCMAIdentifier(id);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configured);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, placed);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier));
    }

    public static void initialize() {
        register("mercuric_sulfide", BlockRegistry.mercuric_sulfide, OreConfiguredFeatures.STONE_ORE_REPLACEABLES, 6, 10, YOffset.getBottom(), YOffset.getTop());
        register("lead_ore", BlockRegistry.lead_ore, OreConfiguredFeatures.STONE_ORE_REPLACEABLES, 4, 10, YOffset.fixed(0), YOffset.fixed(128));
        register("deepslate_lead_ore", BlockRegistry.deepslate_lead_ore, OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, 5, 12, YOffset.getBottom(), YOffset.fixed(0));
        register("silver_ore", BlockRegistry.silver_ore, OreConfiguredFeatures.STONE_ORE_REPLACEABLES, 4, 6, YOffset.fixed(0), YOffset.fixed(96));
        register("deepslate_silver_ore", BlockRegistry.deepslate_silver_ore, OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, 5, 8, YOffset.getBottom(), YOffset.fixed(0));
        register("deepslate_titanium_ore", BlockRegistry.deepslate_titanium_ore, OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, 5, 8, YOffset.getBottom(), YOffset.fixed(-48));
        register("deepslate_moscovium_ore", BlockRegistry.deepslate_moscovium_ore, OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, 3, 5, YOffset.getBottom(), YOffset.fixed(-16));

        LOGGER.info("Ore features loaded");
    }
}
