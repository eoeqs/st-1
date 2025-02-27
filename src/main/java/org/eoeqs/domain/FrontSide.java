package org.eoeqs.domain;

public class FrontSide {
    private boolean isMelted;
    private String meltedDegree;
    private double meltingPoint;

    public FrontSide(boolean isMelted, String meltedDegree, double meltingPoint) {
        this.isMelted = isMelted;
        this.meltedDegree = meltedDegree;
        this.meltingPoint = meltingPoint;
    }

    public boolean isMelted() {
        return isMelted;
    }

    public String getMeltedDegree() {
        return meltedDegree;
    }

    public double getMeltingPoint() {
        return meltingPoint;
    }

    public void updateMeltingState(double temperature) {
        if (temperature >= meltingPoint) {
            isMelted = true;
            meltedDegree = temperature > meltingPoint + 100 ? "almost completely" : "partially";
        }
    }
}