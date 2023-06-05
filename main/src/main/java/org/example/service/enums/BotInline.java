package org.example.service.enums;

public enum BotInline {
    FILM("Полнометражка \uD83C\uDFAC"),
    SERIAL("Сериал \uD83C\uDF7F"),
    CAT_PICTURE("Пикча \uD83D\uDC31"),
    CAT_GIF("Гифка \uD83D\uDE40");



    private final String value;

    BotInline(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static BotInline fromValue(String v) {
        for (BotInline c : BotInline.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
