package com.chatapp.config.chatroom;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.chatapp.server.ChatAppConstants;
import com.chatapp.server.LogHandler;

public class ChatRoomConfigParser {

	public Chatrooms loadChatRooms() {
		Chatrooms chatRooms = null;
		try {
			File file = new File(ChatAppConstants.CHAT_ROOM_CONFIG_FILE);
			JAXBContext jaxbContext = JAXBContext.newInstance(Chatrooms.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			chatRooms = (Chatrooms) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			LogHandler.getLogger().log("Error in parsing the chat room config xml - " + ChatAppConstants.CHAT_ROOM_CONFIG_FILE);
		}

		return chatRooms;
	}

}
