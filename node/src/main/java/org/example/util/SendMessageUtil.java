package org.example.util;

public interface SendMessageUtil {
     void sendAnswerDefoult(String output, long chatId);
     void sendAnswerForFormatLink(String output, long chatId);
     void sendAnswerForFilmInline(long chatId);
     void sendAnswerForFormatLinkFilmAddKeyBoard(String output, long chatId);
}
