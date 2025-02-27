package org.eoeqs.domain;

public class People {
    private String position;
    private String groupDensity;
    private String waitingFor;

    public People(String position, String groupDensity, String waitingFor) {
        this.position = position;
        this.groupDensity = groupDensity;
        this.waitingFor = waitingFor;
    }

    public String getPosition() {
        return position;
    }

    public String getGroupDensity() {
        return groupDensity;
    }

    public String getWaitingFor() {
        return waitingFor;
    }
}