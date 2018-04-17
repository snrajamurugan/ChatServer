package com.chatapp.server;

import java.io.IOException;

import com.chatapp.config.language.LanguageProcessor;
import com.chatapp.util.ChatServerUtil;

public class MessageDispatcher {

	public static void sendMessage(String msg, String chatRoom, LanguageProcessor languageProcessor) {
		try {
			if (msg != null) {
				msg = ChatServerUtil.checkAbuseWordsAndReplace(msg, languageProcessor);
				ConnectionHandler.getHandler().getSendChannel().basicPublish(chatRoom, "", null, msg.getBytes("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
