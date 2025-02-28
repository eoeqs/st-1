package org.eoeqs.domain;

public class Corner {
    private People people;

    public Corner(String location, People people) {
        this.people = people;
    }

    public People getPeople() {
        return people;
    }
}