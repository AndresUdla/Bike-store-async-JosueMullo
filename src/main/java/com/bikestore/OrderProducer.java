package com.bikestore;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            String orderId = UUID.randomUUID().toString();

            Order order = new Order(orderId, "CLIENTE-TEST", LocalDateTime.now().toString());

            Gson gson = new Gson();
            String jsonMessage = gson.toJson(order);

            channel.basicPublish("", QueueConfig.ORDER_QUEUE, null, jsonMessage.getBytes());

            System.out.println("📤 Enviado pedido " + orderId + " → " + jsonMessage);
        }
    }

    static class Order {
        String id;
        String customer;
        String timestamp;

        public Order(String id, String customer, String timestamp) {
            this.id = id;
            this.customer = customer;
            this.timestamp = timestamp;
        }
    }
}
