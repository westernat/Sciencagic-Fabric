package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.mesdag.scma.util.SCMAIdentifier;

public class Groups {
    public static final ItemGroup BLOCKS = FabricItemGroupBuilder.create(new SCMAIdentifier("blocks")).icon(() -> new ItemStack(BlockRegistry.moscovium_block.asItem())).build();
    public static final ItemGroup ITEMS = FabricItemGroupBuilder.create(new SCMAIdentifier("items")).icon(() -> new ItemStack(ItemRegistry.raw_moscovium)).build();
}
