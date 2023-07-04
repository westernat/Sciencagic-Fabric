package org.mesdag.scma.block.energy.entity.machine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.registry.BlockRegistry;
import org.mesdag.scma.registry.ItemRegistry;
import org.mesdag.scma.screen.RollerPressHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;

public class RollerPressEntity extends AbstractMachineEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public static final int size = 4;
    public static final List<Item> roller_press_modules = List.of(Items.DIRT, Items.STONE, Items.COBBLESTONE);
    public static final Map<Item, List<Item>> roller_press_result = Map.of(
        // input: [rod, plate, thread]
        Items.COPPER_INGOT, List.of(ItemRegistry.wooden_pestle, ItemRegistry.terracotta_pestle, ItemRegistry.copper_pestle)
    );


    public RollerPressEntity(BlockPos pos, BlockState state) {
        super(50000, 100, size, 100, BlockRegistry.roller_press_entity, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, RollerPressEntity entity) {
        entity.getEnergyFromBattery();
        ItemStack inputStack = entity.getStack(1);
        ItemStack moduleStack = entity.getStack(2);
        ItemStack outputStack = entity.getStack(3);
        if (entity.hasEnergy() && !inputStack.isEmpty() && !moduleStack.isEmpty()) {
            Item input = inputStack.getItem();
            if (roller_press_result.containsKey(input) && entity.couldContinue() && outputStack.getCount() < outputStack.getMaxCount()) {
                entity.startGenerating();
                entity.tryExtract(5);
            } else if (entity.shouldComplete()) {
                Item module = moduleStack.getItem();
                int index = roller_press_modules.indexOf(module);
                if (index != -1) {
                    Item resultItem = roller_press_result.get(input).get(index);
                    if (outputStack.isEmpty()) {
                        entity.setStack(3, new ItemStack(resultItem, index == 2 ? 2 : 1));
                        inputStack.increment(-1);
                        entity.generationTime = 0;
                    } else if (outputStack.isOf(resultItem)) {
                        outputStack.increment(index == 2 ? 2 : 1);
                        inputStack.increment(-1);
                        entity.generationTime = 0;
                    }
                }
            } else entity.stopGenerating();
        } else entity.stopGenerating();
        entity.updateState(world, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.scma.roller_press");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RollerPressHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if (this.isGenerating) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rolling", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
