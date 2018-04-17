package com.test.chatapp.client;

import org.json.JSONObject;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Client1 {

  private final static String QUEUE_NAME = "server_queue";
  private final static String ROUTING_KEY = "baseball";
  private final static String EXCHANGE_NAME= "chat";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("10.201.53.102");
    factory.setPort(25672);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

    channel.queueBind(QUEUE_NAME,EXCHANGE_NAME , ROUTING_KEY);
//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    AMQP.BasicProperties properties = new AMQP.BasicProperties();
    

    String message = "Hello World! from baseball1";
    JSONObject json = new JSONObject();
    json.put("user", "user2");
    json.put("message", message);
   
    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, json.toString().getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }
}
