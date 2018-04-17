package com.chatapp.server;

import com.chatapp.config.chatroom.ChatRoomConfigParser;
import com.chatapp.config.chatroom.Chatrooms;
import com.chatapp.config.language.LanguageProcessor;
import com.chatapp.config.language.LanguageProcessorConfigParser;

public class Server {

	public Chatrooms activeChatRooms = null;

	public LanguageProcessor languageProcessor = null;

	public static void main(String[] args) {
		Server server = new Server();
		server.loadConfigurationFiles();
		server.initConnectionAndListen();

		System.out.println("Chat server started");
	}

	private void loadConfigurationFiles() {
		ChatRoomConfigParser chatRoomParser = new ChatRoomConfigParser();
		activeChatRooms = chatRoomParser.loadChatRooms();
		
		LanguageProcessorConfigParser languageParser = new LanguageProcessorConfigParser();
		languageProcessor = languageParser.loadLanguageProcessor();
	}

	private void initConnectionAndListen() {
		ConnectionHandler.getHandler().createConnection();
		ConnectionHandler.getHandler().createTopics(activeChatRooms);
		new Thread(new MessageListener(languageProcessor)).start();
	}

}
