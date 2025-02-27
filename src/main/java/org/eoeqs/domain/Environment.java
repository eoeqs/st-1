package org.eoeqs.domain;

public class Environment {
    private double heat;
    private String noise;

    public Environment(double heat, String noise) {
        this.heat = heat;
        this.noise = noise;
    }

    public double getHeat() {
        return heat;
    }

    public String getNoise() {
        return noise;
    }

    public void updateHeat(Bombardment bombardment) {
        if (bombardment.isActive()) {
            this.heat = bombardment.calculateTemperature();
        }
    }
}