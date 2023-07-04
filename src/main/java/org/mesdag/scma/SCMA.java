package org.mesdag.scma;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import org.mesdag.scma.command.Debugger;
import org.mesdag.scma.entity.Amiya;
import org.mesdag.scma.registry.*;
import org.mesdag.scma.screen.MeltingForgingHandler;
import org.mesdag.scma.screen.RollerPressHandler;
import org.mesdag.scma.screen.ThermalGeneratorHandler;
import org.mesdag.scma.util.SCMAIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class SCMA implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("scma");

    @Override
    public void onInitialize() {
        // Initialize
        FluidRegistry.initialize();
        BlockRegistry.initialize();
        ItemRegistry.initialize();
        StatusEffectRegistry.initialize();
        OreFeatureRegistry.initialize();
        CustomFeatureRegistry.initialize();
        EventRegistry.initialize();
        GeckoLib.initialize();


        // Entity
        FabricDefaultAttributeRegistry.register(ServerEntityRegistry.amiya, Amiya.createAmiyaAttributes());
        LOGGER.info("Server entities loaded");


        // Command
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> Debugger.register(dispatcher));
        LOGGER.info("Commands loaded");
    }

    // Screen
    public static final ScreenHandlerType<ThermalGeneratorHandler> thermal_generator_handler = ScreenHandlerRegistry.registerSimple(new SCMAIdentifier("thermal_generator_handler"), ThermalGeneratorHandler::new);
    public static final ScreenHandlerType<RollerPressHandler> roller_press_handler = ScreenHandlerRegistry.registerSimple(new SCMAIdentifier("roller_press_handler"), RollerPressHandler::new);
    public static final ScreenHandlerType<MeltingForgingHandler> melting_forging_handler = ScreenHandlerRegistry.registerSimple(new SCMAIdentifier("melting_forging_handler"), MeltingForgingHandler::new);
}