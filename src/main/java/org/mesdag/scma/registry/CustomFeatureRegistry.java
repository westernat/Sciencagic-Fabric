package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.mesdag.scma.feature.BaseLakeFeature;
import org.mesdag.scma.util.SCMAIdentifier;

import java.util.List;
import java.util.function.Predicate;

import static org.mesdag.scma.SCMA.LOGGER;

public class CustomFeatureRegistry {
    private static int percent(int value) {
        return 100 / value;
    }

    private static <F extends Feature<DefaultFeatureConfig>> void defaultFeatureRegister(String id, F customFeature, List<PlacementModifier> placementModifierList, Predicate<BiomeSelectionContext> biomePredicate, GenerationStep.Feature generationStep) {
        Identifier identifier = new SCMAIdentifier(id);
        Feature<DefaultFeatureConfig> feature = Registry.register(Registry.FEATURE, identifier, customFeature);
        ConfiguredFeature<?, ?> configured = new ConfiguredFeature<>(feature, new DefaultFeatureConfig());
        PlacedFeature placed = new PlacedFeature(RegistryEntry.of(configured), placementModifierList);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configured);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, placed);
        BiomeModifications.addFeature(biomePredicate, generationStep, RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier));
    }

    public static void initialize() {
        defaultFeatureRegister(
            "petroleum_lake",
            new BaseLakeFeature(FluidRegistry.petroleum.getDefaultState(), Blocks.TUFF.getDefaultState()),
            List.of(RarityFilterPlacementModifier.of(percent(10)), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(-48), YOffset.fixed(80))),
            BiomeSelectors.categories(Biome.Category.DESERT),
            GenerationStep.Feature.LOCAL_MODIFICATIONS
        );

        LOGGER.info("Custom features loaded");
    }
}
