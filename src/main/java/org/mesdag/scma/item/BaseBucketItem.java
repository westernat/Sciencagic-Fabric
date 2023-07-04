package org.mesdag.scma.item;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class BaseBucketItem extends BucketItem {
    public BaseBucketItem(Fluid fluid) {
        super(fluid, new Item.Settings().group(ITEMS).recipeRemainder(Items.BUCKET).maxCount(1));
    }
}
