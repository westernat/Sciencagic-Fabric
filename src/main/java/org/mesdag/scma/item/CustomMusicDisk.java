package org.mesdag.scma.item;

import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class CustomMusicDisk extends MusicDiscItem {
    public CustomMusicDisk(int comparatorOutput, SoundEvent sound, Item.Settings settings) {
        super(comparatorOutput, sound, settings);
    }
}
