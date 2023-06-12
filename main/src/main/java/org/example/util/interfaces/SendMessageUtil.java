package org.example.util.interfaces;

public interface SendMessageUtil {
    void sendAnswerDefault(String output, long chatId);

    void sendAnswerForFormatLink(String output, long chatId);

    void sendAnswerForFilmInline(String output, long chatId);
}
