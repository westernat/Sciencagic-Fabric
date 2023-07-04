package org.mesdag.scma.block.energy.entity.machine;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.mesdag.scma.block.energy.entity.IEnergy;

public interface IMachine extends IEnergy {
    default void tryInsert(long amount) {
        try (Transaction t = Transaction.openOuter()) {
            getSelf().insert(amount, t);
            t.commit();
        }
    }

    default long maxInsert() {
        return Math.min(getSelf().maxInsert, getSelf().capacity - getSelf().amount);
    }

    default void tryExtract(long amount) {
        try (Transaction t = Transaction.openOuter()) {
            getSelf().extract(amount, t);
            t.commit();
        }
    }

    default boolean hasEnergy() {
        return getSelf().amount > 0;
    }
}
