package org.mesdag.scma.block.energy.entity.generator;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.mesdag.scma.block.energy.entity.IEnergy;

public interface IGenerator extends IEnergy {
    default void tryInsert(long amount) {
        try (Transaction t = Transaction.openOuter()) {
            getSelf().insert(amount, t);
            t.commit();
        }
    }

    default long maxInsert(long maxAmount) {
        return Math.min(getSelf().maxInsert, Math.min(maxAmount, getSelf().capacity - getSelf().amount));
    }

    default void tryExtract(long amount) {
        try (Transaction t = Transaction.openOuter()) {
            getSelf().extract(amount, t);
            t.commit();
        }
    }

    default long maxExtract() {
        return Math.min(getSelf().maxExtract, getSelf().amount);
    }

    default boolean hasEnergy() {
        return getSelf().amount > 0;
    }

    default long getStoredEnergy() {
        return getSelf().amount;
    }
}
