package org.example.service.enums;

public enum BotCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String value;

    BotCommands(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static BotCommands fromValue(String v) {
        for (BotCommands c : BotCommands.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
