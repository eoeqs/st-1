package org.eoeqs.domain;

public class MoltenMetalStreams {
    private String density;
    private String movement;

    public MoltenMetalStreams(String density, String movement) {
        this.density = density;
        this.movement = movement;
    }

    public String getDensity() {
        return density;
    }

    public String getMovement() {
        return movement;
    }
}
