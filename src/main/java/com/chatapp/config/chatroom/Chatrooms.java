package com.chatapp.config.chatroom;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "chatrooms")
@XmlAccessorType(XmlAccessType.FIELD)
public class Chatrooms {

	@XmlElement(name = "chatroom")
	private ArrayList<ChatRoom> chatrooms;

	public ArrayList<ChatRoom> getRooms() {
		return chatrooms;
	}

	public void setRows(ArrayList<ChatRoom> chatrooms) {
		this.chatrooms = chatrooms;
	}

}
