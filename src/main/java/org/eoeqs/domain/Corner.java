package org.eoeqs.domain;

public class Corner {
    private String location;
    private People people;

    public Corner(String location, People people) {
        this.location = location;
        this.people = people;
    }

    public String getLocation() {
        return location;
    }

    public People getPeople() {
        return people;
    }
}