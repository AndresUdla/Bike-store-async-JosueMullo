package com.bikestore;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueConfig {

    public static final String ORDER_QUEUE = "orders";
    public static final String EMAIL_QUEUE = "email_notifications";
    public static final String DEAD_LETTER_QUEUE = "dead_orders";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Cola principal de pedidos
            channel.queueDeclare(ORDER_QUEUE, true, false, false, null);

            // Cola donde irán los pedidos que fallaron 3 veces
            channel.queueDeclare(DEAD_LETTER_QUEUE, true, false, false, null);

            // Cola de email
            channel.queueDeclare(EMAIL_QUEUE, true, false, false, null);

            System.out.println("✅ Colas creadas correctamente.");
        }
    }
}
