import express from "express";

import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/inicialData.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";

import checkToken from "./src/config/auth/checkToken.js";
import saleRoutes from "./src/modules/sale/routes/SaleRoutes.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;
const CONTAINER_ENV = "container";
const THREE_MINUTES = 180000;

startApplication();

async function startApplication() {
  if (CONTAINER_ENV === env.NODE_ENV) {
    console.info("Waiting for RabbitMQ and MongoDB containers to start...");
    setInterval(() => {
      connectMongoDb();
      connectRabbitMq();
    }, THREE_MINUTES);
  } else {
    connectMongoDb();
    createInitialData();
    connectRabbitMq();
  }
}

app.use(express.json());

app.get("/api/status", (req, res) => {
  return res.status(200).json(getOkResponse());
});

app.use(checkToken);
app.use(saleRoutes);

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});