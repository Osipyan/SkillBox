package main.api.response.model;

public class Tag {
    private String name;
    private double weight;

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public Tag setWeight(double weight) {
        this.weight = weight;
        return this;
    }
}
