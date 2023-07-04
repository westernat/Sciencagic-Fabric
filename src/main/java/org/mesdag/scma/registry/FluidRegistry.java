package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.fluid.Mercury;
import org.mesdag.scma.fluid.Naphtha;
import org.mesdag.scma.fluid.Petroleum;
import org.mesdag.scma.item.BaseBucketItem;
import org.mesdag.scma.util.SCMAIdentifier;

import static org.mesdag.scma.SCMA.LOGGER;

public class FluidRegistry {
    public static FlowableFluid still_petroleum;
    public static FlowableFluid flowing_petroleum;
    public static FluidBlock petroleum;
    public static BucketItem petroleum_bucket;

    public static FlowableFluid still_naphtha;
    public static FlowableFluid flowing_naphtha;
    public static FluidBlock naphtha;
    public static BucketItem naphtha_bucket;

    public static FlowableFluid still_mercury;
    public static FlowableFluid flowing_mercury;
    public static FluidBlock mercury;
    public static BucketItem mercury_bottle;


    public static void initialize() {
        still_petroleum = Registry.register(Registry.FLUID, new SCMAIdentifier("still_petroleum"), new Petroleum.Still());
        flowing_petroleum = Registry.register(Registry.FLUID, new SCMAIdentifier("flowing_petroleum"), new Petroleum.Flowing());
        petroleum = Registry.register(Registry.BLOCK, new SCMAIdentifier("petroleum"), new FluidBlock(still_petroleum, FabricBlockSettings.of((new Material.Builder(MapColor.BLACK)).allowsMovement().notSolid().replaceable().liquid().build()).noCollision().strength(100.0F).dropsNothing()));
        petroleum_bucket = Registry.register(Registry.ITEM, new SCMAIdentifier("petroleum_bucket"), new BaseBucketItem(still_petroleum));

        still_naphtha = Registry.register(Registry.FLUID, new SCMAIdentifier("still_naphtha"), new Naphtha.Still());
        flowing_naphtha = Registry.register(Registry.FLUID, new SCMAIdentifier("flowing_naphtha"), new Naphtha.Flowing());
        naphtha = Registry.register(Registry.BLOCK, new SCMAIdentifier("naphtha"), new FluidBlock(still_naphtha, FabricBlockSettings.copyOf(Blocks.WATER)));
        naphtha_bucket = Registry.register(Registry.ITEM, new SCMAIdentifier("naphtha_bucket"), new BaseBucketItem(still_naphtha));

        still_mercury = Registry.register(Registry.FLUID, new SCMAIdentifier("still_mercury"), new Mercury.Still());
        flowing_mercury = Registry.register(Registry.FLUID, new SCMAIdentifier("flowing_mercury"), new Mercury.Flowing());
        mercury = Registry.register(Registry.BLOCK, new SCMAIdentifier("mercury"), new FluidBlock(still_mercury, FabricBlockSettings.copyOf(Blocks.POWDER_SNOW)));
        mercury_bottle = Registry.register(Registry.ITEM, new SCMAIdentifier("mercury_bottle"), new BaseBucketItem(still_mercury));

        LOGGER.info("Fluid loaded");
    }
}
