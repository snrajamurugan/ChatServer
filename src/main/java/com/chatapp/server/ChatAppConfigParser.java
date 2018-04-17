package com.chatapp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ChatAppConfigParser {

	private Properties configFile;

	private static ChatAppConfigParser instance;

	private ChatAppConfigParser() {
		configFile = new Properties();
		try {
			configFile.load(new InputStreamReader(
					new FileInputStream(new File(ChatAppConstants.SERVER_CONFIG_PROPERTIES_FILE))));
		} catch (Exception e) {
			LogHandler.getLogger().log("Error loading the server configuration - " + e.getMessage());
		}
	}

	private String getValue(String key) {
		return configFile.getProperty(key);
	}

	public static String getProperty(String key) {
		if (instance == null) {
			instance = new ChatAppConfigParser();
		}
		return instance.getValue(key);
	}

}
