package com.chatapp.util;

import com.chatapp.config.language.AbuseWord;
import com.chatapp.config.language.LanguageProcessor;

public class ChatServerUtil {

	public static String checkAbuseWordsAndReplace(String msg, LanguageProcessor processor) {
		for (AbuseWord word : processor.getRows()) {
			msg = msg.replace(word.getName(), word.getReplacement());
		}

		return msg;
	}

}
