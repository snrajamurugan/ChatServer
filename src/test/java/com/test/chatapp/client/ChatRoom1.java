package com.test.chatapp.client;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ChatRoom1 {

  private static final String TASK_QUEUE_NAME = "baseball";
  
  private final static String EXCHANGE_NAME= "football";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("10.201.53.102");
    factory.setPort(25672);
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();
//	channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
//   channel.queueBind(TASK_QUEUE_NAME, EXCHANGE_NAME, "");
    //channel.queueBind(TASK_QUEUE_NAME, true, false, false, null);
    
    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");

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
    channel.basicConsume(queueName, false, consumer);
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
