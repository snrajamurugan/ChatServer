package com.chatapp.server;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.chatapp.config.language.LanguageProcessor;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class MessageListener implements Runnable, Consumer {
	
	private String SERVER_QUEUE_NAME = "server_queue";

	private LanguageProcessor languageProcessor;
	
	public MessageListener(LanguageProcessor languageProcessor) {
		this.languageProcessor = languageProcessor;
	}
	
	public void run() {
		try {
			ConnectionHandler.getHandler().getReceiveChannel().exchangeDeclare("chat", BuiltinExchangeType.DIRECT);
			ConnectionHandler.getHandler().getReceiveChannel().queueBind(SERVER_QUEUE_NAME, "chat", "");

			ConnectionHandler.getHandler().getReceiveChannel().basicConsume(SERVER_QUEUE_NAME, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		JSONObject json = null;
		String message = new String(body, "UTF-8");
		try {
			json = new JSONObject(message);

			System.out.println(
					" [x] Received '" + json.get("user") + "'" + json.get("message") + "'" + env.getRoutingKey());
			LogHandler.getLogger().log(env.getRoutingKey(), (String) json.get("message") , (String) json.get("user"));
			MessageDispatcher.sendMessage(message, env.getRoutingKey(), languageProcessor);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancel(String arg0) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleCancelOk(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleRecoverOk(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
		// TODO Auto-generated method stub
	}

}
