package com.bikestore;

import com.google.gson.Gson;
import com.rabbitmq.client.*;

import java.util.Random;

public class PaymentWorker {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(QueueConfig.ORDER_QUEUE, false, (tag, delivery) -> {

            String json = new String(delivery.getBody());
            System.out.println("💳 Procesando pago → " + json);

            boolean pagoExitoso = new Random().nextBoolean(); // 50/50

            if (pagoExitoso) {
                System.out.println("✅ Pago APROBADO");

                channel.basicPublish("", QueueConfig.EMAIL_QUEUE, null, delivery.getBody());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            } else {
                System.out.println("❌ Pago FALLÓ, reintentando...");

                // <-- Aquí el cambio
                if (delivery.getEnvelope().isRedeliver()) {
                    System.out.println("🚨 Enviando a Dead-Letter Queue");
                    channel.basicPublish("", QueueConfig.DEAD_LETTER_QUEUE, null, delivery.getBody());
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else {
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            }

        }, tag -> {});
    }
}
