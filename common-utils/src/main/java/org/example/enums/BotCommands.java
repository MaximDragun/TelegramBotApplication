package org.example.enums;

public enum BotCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start"),
    RESEND("/resend_email"),
    CHOOSE_ANOTHER_EMAIL("/change_email"),
    WHAT_TO_SEE("/what_to_watch"),
    CAT_PICTURE("/cat");

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
