package org.mesdag.scma.registry;


import org.mesdag.scma.block.energy.network.NetworkService;
import org.mesdag.scma.block.energy.network.PathService;

public class EventRegistry {
    public static void initialize() {
        NetworkService.INSTANCE.initialize();
        PathService.INSTANCE.initialize();
    }
}
