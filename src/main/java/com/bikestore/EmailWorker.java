package com.bikestore;

import com.rabbitmq.client.*;

public class EmailWorker {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(QueueConfig.EMAIL_QUEUE, true, (tag, delivery) -> {
            String json = new String(delivery.getBody());
            System.out.println("📧 Enviando email de confirmación → " + json);
        }, tag -> {});
    }
}
