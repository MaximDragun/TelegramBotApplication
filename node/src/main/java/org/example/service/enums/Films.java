package org.example.service.enums;

public enum Films {
    FILM("Полнометражка \uD83C\uDFAC"),
    SERIAL("Сериал \uD83C\uDF7F");


    private final String value;

    Films(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Films fromValue(String v) {
        for (Films c : Films.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
