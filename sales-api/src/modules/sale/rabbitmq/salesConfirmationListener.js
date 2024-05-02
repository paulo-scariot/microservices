import amqp from "amqplib/callback_api.js";
import { RABBIT_MQ_URL } from "../../../config/constants/secrets.js";
import { SALES_CONFIRMATION_QUEUE } from "../../../config/rabbitmq/queue.js";
import SaleService from "../service/SaleService.js";

export function listenToSalesConfirmationQueue() {
  amqp.connect(RABBIT_MQ_URL, (error, connection) => {
    if (error) {
      throw error;
    }

    console.info("Listening to sales confirmation queue...");

    connection.createChannel((error, channel) => {
      if (error) {
        throw error;
      }

      channel.consume(SALES_CONFIRMATION_QUEUE, (message) => {
        console.info(`Recieved from queue "${SALES_CONFIRMATION_QUEUE}" the message "${message.content.toString()}"`);
        SaleService.updateSale(message.content);
      }, { noAck: true })
    })
  });
}