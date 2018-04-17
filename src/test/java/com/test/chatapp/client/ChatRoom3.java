package com.test.chatapp.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ChatRoom3 {

  private static final String TASK_QUEUE_NAME = "room1";
  
  private final static String QUEUE_NAME = "server_queue";
  private final static String ROUTING_KEY = "room1";
  private final static String EXCHANGE_NAME= "chat";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("10.201.53.102");
    factory.setPort(25672);
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();
	//channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
   channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
    //channel.queueBind(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(1);

    final Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");

        System.out.println(" [x] Received '" + message + "'");
        try {
          doWork(message);
        } finally {
          System.out.println(" [x] Done");
          channel.basicAck(envelope.getDeliveryTag(), false);
        }
      }
    };
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
  }

  private static void doWork(String task) {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException _ignored) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
