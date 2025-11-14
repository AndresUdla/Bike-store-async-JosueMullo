📝 README – BikeStore Async (Java + RabbitMQ)
📌 Descripción

Este proyecto implementa un flujo asíncrono para procesar pedidos de una tienda de bicicletas utilizando RabbitMQ como bróker de mensajería.
La idea es simular un pequeño sistema distribuido donde el pedido pasa por varios servicios independientes:

Un productor que publica el pedido.

Un worker de pagos que valida la transacción.

Un worker de e-mail que envía la confirmación.

Y una Dead Letter Queue para manejar pedidos fallidos después de varios intentos.

El patrón principal es:
Producer → Queue → Workers → Dead Letter Queue.

✅ Requisitos Previos

Antes de ejecutar el proyecto necesitas instalar:

1. Erlang

RabbitMQ funciona sobre Erlang.
Descárgalo aquí:
https://www.erlang.org/downloads

Instálalo normal (Siguiente → Siguiente).

2. RabbitMQ Server

Descarga e instala RabbitMQ:
https://www.rabbitmq.com/download.html

Para activar la consola web, abre PowerShell como administrador y ejecuta:

rabbitmq-plugins enable rabbitmq_management


Luego reinicia el servicio:

net stop RabbitMQ
net start RabbitMQ

3. Acceder al panel de administración

En tu navegador ingresa a:

http://localhost:15672

Credenciales por defecto:

Usuario	Contraseña
guest	guest
🚀 Ejecución del Proyecto

El sistema está dividido en 4 clases principales, cada una representa un servicio que se ejecuta de forma independiente (abre una ventana de terminal por clase):

Clase	Función
QueueConfig	Crea las colas, exchanges y DLQ.
PaymentWorker	Procesa pagos y simula éxito/fallo.
EmailWorker	Envía el correo solo si el pago fue exitoso.
OrderProducer	Envía nuevos pedidos al sistema.

Orden recomendado de ejecución:

QueueConfig

PaymentWorker

EmailWorker

OrderProducer

🔄 Flujo del Sistema

1️⃣ OrderProducer manda un pedido → Cola orders
2️⃣ PaymentWorker recibe el mensaje:

50% chance de pago aprobado → pasa a email_notifications

50% chance de fallo → intenta reintentar
3️⃣ Si falla 3 veces, el pedido va a la cola:
dead_orders
4️⃣ EmailWorker envía la notificación si el pago fue exitoso

Cada evento imprime en consola:
🟦 pedidoId
🕒 timestamp
🧵 thread actual

Para ayudarte a depurar fácilmente.

🪦 Dead Letter Queue (Pedidos fallidos)

Cuando un pago falla repetidamente, verás en consola algo como:

🚨 Enviando a Dead-Letter Queue


Puedes revisar esos pedidos en el panel de RabbitMQ:

➡️ Queues → dead_orders

Ahí aparecerán los mensajes que no pudieron procesarse correctamente.
