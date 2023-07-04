package org.mesdag.scma.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.util.SCMAIdentifier;

public class SoundEventRegistry {
    public static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new SCMAIdentifier(id)));
    }
}
