import express from "express";

import { connectMongoDb } from "./src/config/db/mongoDbConfig.js";
import { createInitialData } from "./src/config/db/inicialData.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js";

import checkToken from "./src/config/auth/checkToken.js";
import saleRoutes from "./src/modules/sale/routes/SaleRoutes.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

startApplication();

async function startApplication() {
  setTimeout(() => {
    connectMongoDb();
    createInitialData();
    connectRabbitMq();
  }, 20000);
}

app.use(express.json());

app.get("/api/status", (req, res) => {
  return res.status(200).json({
    service: "Sales-API",
    status: "up",
    httpStatus: 200
  });
});

app.use(checkToken);
app.use(saleRoutes);

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});