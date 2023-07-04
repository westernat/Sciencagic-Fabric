package org.mesdag.scma.util;

import net.minecraft.util.Identifier;

public class SCMAIdentifier extends Identifier {
    public SCMAIdentifier(String id) {
        super(new String[]{"scma", id});
    }
}
