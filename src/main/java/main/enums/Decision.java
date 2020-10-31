package main.enums;

public enum Decision {
    ACCEPT ("accept"),
    DECLINE ("decline");

    private String value;

    Decision(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
