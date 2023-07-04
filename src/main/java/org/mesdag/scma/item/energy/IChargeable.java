package org.mesdag.scma.item.energy;

public interface IChargeable {
    long getCapacity();

    long getEnergyIO();

    default long maxInsert(long maxAmount, long storedEnergy) {
        return Math.min(getEnergyIO(), Math.min(maxAmount, getCapacity() - storedEnergy));
    }

    default long maxExtract(long maxAmount, long storedEnergy) {
        return Math.min(maxAmount, storedEnergy);
    }
}
