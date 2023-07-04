package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.block.MoscoviumOre;
import org.mesdag.scma.block.PortalFrame;
import org.mesdag.scma.block.PortalFrameEntity;
import org.mesdag.scma.block.energy.entity.generator.MoscoviumBlockEntity;
import org.mesdag.scma.block.energy.entity.generator.SolarPanelEntity;
import org.mesdag.scma.block.energy.entity.generator.ThermalGeneratorEntity;
import org.mesdag.scma.block.energy.entity.logic.ESAdapterEntity;
import org.mesdag.scma.block.energy.entity.machine.MeltingForgingEntity;
import org.mesdag.scma.block.energy.entity.machine.RollerPressEntity;
import org.mesdag.scma.block.energy.generator.MoscoviumBlock;
import org.mesdag.scma.block.energy.generator.SolarPanel;
import org.mesdag.scma.block.energy.generator.ThermalGenerator;
import org.mesdag.scma.block.energy.logic.*;
import org.mesdag.scma.block.energy.machine.MeltingForging;
import org.mesdag.scma.block.energy.machine.RollerPress;
import org.mesdag.scma.block.energy.wire.CopperWire;
import org.mesdag.scma.block.energy.wire.CopperWireEntity;
import org.mesdag.scma.block.magical.AlchemyTable;
import org.mesdag.scma.block.magical.AlchemyTableEntity;
import org.mesdag.scma.block.nature.ThornVine;
import org.mesdag.scma.block.tank.FluidTank;
import org.mesdag.scma.block.tank.FluidTankEntity;
import org.mesdag.scma.block.tool.PestleDish;
import org.mesdag.scma.block.tool.PestleDishEntity;
import org.mesdag.scma.util.SCMAIdentifier;

import static org.mesdag.scma.SCMA.LOGGER;
import static org.mesdag.scma.registry.Groups.BLOCKS;

public class BlockRegistry {
    // Logic
    public static final LedLamp led_lamp = new LedLamp();
    public static final ESAdapter es_adapter = new ESAdapter();
    public static final BlockEntityType<ESAdapterEntity> es_adapter_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("es_adapter_entity"), FabricBlockEntityTypeBuilder.create(ESAdapterEntity::new, es_adapter).build());


    // Machine
    public static final CopperWire copper_wire = new CopperWire();
    public static final BlockEntityType<CopperWireEntity> copper_wire_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("wire_entity"), FabricBlockEntityTypeBuilder.create(CopperWireEntity::new, copper_wire).build());
    public static final SolarPanel solar_panel = new SolarPanel();
    public static final BlockEntityType<SolarPanelEntity> solar_panel_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("solar_panel_entity"), FabricBlockEntityTypeBuilder.create(SolarPanelEntity::new, solar_panel).build());
    public static final ThermalGenerator thermal_generator = new ThermalGenerator();
    public static final BlockEntityType<ThermalGeneratorEntity> thermal_generator_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("thermal_generator_entity"), FabricBlockEntityTypeBuilder.create(ThermalGeneratorEntity::new, thermal_generator).build());
    public static final RollerPress roller_press = new RollerPress();
    public static final BlockEntityType<RollerPressEntity> roller_press_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("roller_press_entity"), FabricBlockEntityTypeBuilder.create(RollerPressEntity::new, roller_press).build());
    public static final MeltingForging melting_forging = new MeltingForging();
    public static final BlockEntityType<MeltingForgingEntity> melting_forging_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("melting_forging_entity"), FabricBlockEntityTypeBuilder.create(MeltingForgingEntity::new, melting_forging).build());


    // Ore
    public static final Block mercuric_sulfide = new Block(FabricBlockSettings.of(Material.STONE).strength(1.0f).sounds(BlockSoundGroup.STONE));
    public static final Block lead_ore = new Block(FabricBlockSettings.of(Material.STONE).strength(2.0f).sounds(BlockSoundGroup.STONE));
    public static final Block deepslate_lead_ore = new Block(FabricBlockSettings.of(Material.STONE).strength(2.5F).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Block silver_ore = new Block(FabricBlockSettings.of(Material.STONE).strength(2.5F).sounds(BlockSoundGroup.STONE));
    public static final Block deepslate_silver_ore = new Block(FabricBlockSettings.of(Material.STONE).strength(3.0F).sounds(BlockSoundGroup.DEEPSLATE));
    public static final Block deepslate_titanium_ore = new Block(FabricBlockSettings.of(Material.STONE).strength(3.5F).sounds(BlockSoundGroup.DEEPSLATE));
    public static final MoscoviumOre deepslate_moscovium_ore = new MoscoviumOre();
    public static final MoscoviumBlock moscovium_block = new MoscoviumBlock();
    public static final BlockEntityType<MoscoviumBlockEntity> moscovium_block_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("moscovium_block_entity"), FabricBlockEntityTypeBuilder.create(MoscoviumBlockEntity::new, moscovium_block).build());


    // Tool
    public static final PestleDish pestle_dish = new PestleDish();
    public static final BlockEntityType<PestleDishEntity> pestle_dish_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("pestle_dish_entity"), FabricBlockEntityTypeBuilder.create(PestleDishEntity::new, pestle_dish).build());
    public static final AlchemyTable alchemy_table = new AlchemyTable();
    public static final BlockEntityType<AlchemyTableEntity> alchemy_table_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("alchemy_table_entity"), FabricBlockEntityTypeBuilder.create(AlchemyTableEntity::new, alchemy_table).build());
    public static final PortalFrame portal_frame = new PortalFrame();
    public static final BlockEntityType<PortalFrameEntity> portal_frame_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("portal_frame_entity"), FabricBlockEntityTypeBuilder.create(PortalFrameEntity::new, portal_frame).build());


    // Tank
    public static final FluidTank fluid_tank = new FluidTank();
    public static final BlockEntityType<FluidTankEntity> fluid_tank_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, new SCMAIdentifier("fluid_tank_entity"), FabricBlockEntityTypeBuilder.create(FluidTankEntity::new, fluid_tank).build());


    private static void register(String id, Block block) {
        Registry.register(Registry.ITEM, new SCMAIdentifier(id), new BlockItem(block, new Item.Settings().group(BLOCKS)));
        Registry.register(Registry.BLOCK, new SCMAIdentifier(id), block);
    }

    public static void initialize() {
        register("logic_cable", new LogicCable());
        register("led_lamp", led_lamp);
        register("logic_lever", new LogicLever());
        register("add_gate", new AddGate());
        register("and_gate", new AndGate());
        register("diode_gate", new DiodeGate());
        register("not_gate", new NotGate());
        register("or_gate", new OrGate());
        register("pentode_gate", new PentodeGate());
        register("reduce_gate", new ReduceGate());
        register("switch_gate", new SwitchGate());
        register("triode_gate", new TriodeGate());
        register("xor_gate", new XorGate());
        register("rs_adapter", new RSAdapter());
        register("es_adapter", es_adapter);

        register("copper_wire", copper_wire);
        register("mechanical_shell", new Block(FabricBlockSettings.of(Material.METAL).strength(1.0f).sounds(BlockSoundGroup.METAL)));
        register("solar_panel", solar_panel);
        register("thermal_generator", thermal_generator);
        register("roller_press", roller_press);
        register("melting_forging", melting_forging);

        register("mercuric_sulfide", mercuric_sulfide);
        register("lead_ore", lead_ore);
        register("deepslate_lead_ore", deepslate_lead_ore);
        register("lead_block", new Block(FabricBlockSettings.of(Material.METAL).strength(2.5F).sounds(BlockSoundGroup.METAL)));
        register("silver_ore", silver_ore);
        register("deepslate_silver_ore", deepslate_silver_ore);
        register("silver_block", new Block(FabricBlockSettings.of(Material.METAL).strength(3.0F).sounds(BlockSoundGroup.METAL)));
        register("deepslate_titanium_ore", deepslate_titanium_ore);
        register("titanium_block", new Block(FabricBlockSettings.of(Material.METAL).strength(3.5F).sounds(BlockSoundGroup.METAL)));
        register("deepslate_moscovium_ore", deepslate_moscovium_ore);
        register("moscovium_block", moscovium_block);

        register("pestle_dish", pestle_dish);
        register("alchemy_table", alchemy_table);
        register("portal_frame", portal_frame);

        register("fluid_tank", fluid_tank);

        register("thorn_vine", new ThornVine());

        LOGGER.info("Blocks loaded");
    }
}