package org.example.util.interfaces;

public interface SendMessageUtil {
     void sendAnswerDefault(String output, long chatId);
     void sendAnswerForFormatLink(String output, long chatId);
     void sendAnswerForFilmInline(long chatId);
     void sendAnswerForFilmInlineWithLink(String output, long chatId);
     void sendStartKeyBoard(String output,long chatId);
}
