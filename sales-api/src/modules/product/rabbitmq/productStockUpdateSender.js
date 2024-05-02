import amqp from "amqplib/callback_api.js";
import { RABBIT_MQ_URL } from "../../../config/constants/secrets.js";
import { PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_ROUTINGKEY } from "../../../config/rabbitmq/queue.js";

export function sendMessageToProductStockUpdateQueue(message) {
  amqp.connect(RABBIT_MQ_URL, (error, connection) => {
    if (error) {
      throw error;
    }

    console.info("Listening to sales confirmation queue...");

    connection.createChannel((error, channel) => {
      if (error) {
        throw error;
      }

      let jsonMessage = JSON.stringify(message);
      console.info(`Sending message to product update stock: ${jsonMessage}`);
      channel.publish(PRODUCT_TOPIC, PRODUCT_STOCK_UPDATE_ROUTINGKEY, Buffer.from(jsonMessage));
      console.info(`Message was sent.`);
    })
  });
}