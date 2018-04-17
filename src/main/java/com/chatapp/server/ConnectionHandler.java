package com.chatapp.server;

import java.io.IOException;

import com.chatapp.config.chatroom.ChatRoom;
import com.chatapp.config.chatroom.Chatrooms;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionHandler {

	private String ipAddress;

	private String port;

	private Connection connection;

	private Channel sendChannel;

	private Channel receiveChannel;

	private static ConnectionHandler handler = null;

	private ConnectionHandler() {
		ipAddress = ChatAppConfigParser.getProperty(ChatAppConstants.SERVER_IP_PROPERTY);
		port = ChatAppConfigParser.getProperty(ChatAppConstants.SERVER_PORT_PROPERTY);
	}

	public static ConnectionHandler getHandler() {
		if (handler == null) {
			handler = new ConnectionHandler();
		}

		return handler;
	}

	public Channel getSendChannel() {
		return sendChannel;
	}

	public Channel getReceiveChannel() {
		return receiveChannel;
	}

	public void createConnection() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ipAddress);
			factory.setPort(Integer.parseInt(port));
			connection = factory.newConnection();
			receiveChannel = connection.createChannel();
			sendChannel = connection.createChannel();
		} catch (Exception e) {
			LogHandler.getLogger().log("Error in creating the connection - " + e.getMessage());
		}
	}

	public void closeConnection() {
		try {
			if (this.receiveChannel != null) {
				this.receiveChannel.close();
			}
			if (this.sendChannel != null) {
				this.sendChannel.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (Exception e) {
			LogHandler.getLogger().log("Error in closing the connection - " + e.getMessage());
		}
	}

	public void createTopics(Chatrooms rooms) {
		try {
			for (ChatRoom room : rooms.getRooms()) {
				sendChannel.exchangeDeclare(room.getName(), "topic");
			}
			receiveChannel.queueDeclare(ChatAppConstants.QUEUE_NAME, false, false, false, null);
		} catch (IOException e) {
			LogHandler.getLogger().log("Error in creating the topic - " + e.getMessage());
		}
	}

}
