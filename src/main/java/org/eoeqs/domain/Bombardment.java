package org.eoeqs.domain;

public class Bombardment {
    private boolean isActive;
    private String intensity;
    private int duration;

    public Bombardment(boolean isActive, String intensity, int duration) {
        this.isActive = isActive;
        this.intensity = intensity;
        this.duration = duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getIntensity() {
        return intensity;
    }

    public int getDuration() {
        return duration;
    }

    public double calculateTemperature() {
        double baseTemp = intensity.equals("high") ? 1000 : 500;
        return baseTemp + duration * 10;
    }
}