import amqp from "amqplib/callback_api.js";

import {
  PRODUCT_TOPIC,
  PRODUCT_STOCK_UPDATE_QUEUE,
  PRODUCT_STOCK_UPDATE_ROUTINGKEY,
  SALES_CONFIRMATION_QUEUE,
  SALES_CONFIRMATION_ROUTINGKEY
} from './queue.js'

import { RABBIT_MQ_URL } from "../constants/secrets.js"
import { listenToSalesConfirmationQueue } from "../../modules/sale/rabbitmq/salesConfirmationListener.js";

const TWO_SECOND = 2000;

export async function connectRabbitMq() {
  connectRabbitMqAndCreateQueues();
}

function connectRabbitMqAndCreateQueues() {
  amqp.connect(RABBIT_MQ_URL, { timeout: 180000 }, (error, connection) => {
    if (error) {
      throw error;
    }

    console.info("Starting RabbitMQ...");
    createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTINGKEY, PRODUCT_TOPIC);
    createQueue(connection, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTINGKEY, PRODUCT_TOPIC);

    console.info("Queues and Topics were defined.")

    setTimeout(() => {
      connection.close();
    }, TWO_SECOND);
  });
  setTimeout(() => {
    listenToSalesConfirmationQueue();
  }, TWO_SECOND);
}

function createQueue(connection, queue, routingKey, topic) {
  connection.createChannel((error, channel) => {
    if (error) {
      throw error;
    }
    channel.assertExchange(topic, 'topic', { durable: true });
    channel.assertQueue(queue, { durable: true });
    channel.bindQueue(queue, topic, routingKey);
  });
}