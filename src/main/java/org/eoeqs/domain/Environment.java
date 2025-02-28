package org.eoeqs.domain;

public class Environment {
    private double heat;

    public Environment(double heat, String noise) {
        this.heat = heat;
    }

    public double getHeat() {
        return heat;
    }

    public void updateHeat(Bombardment bombardment) {
        if (bombardment.isActive()) {
            this.heat = bombardment.calculateTemperature();
        }
    }
}