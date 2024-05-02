import { Router } from "express";

import SaleController from "../controller/SaleController.js";

const router = new Router();

router.get("/api/sale/:id", SaleController.findById);
router.get("/api/sale/product/:productId", SaleController.findByProductId);
router.get("/api/sale", SaleController.findAll);
router.post("/api/sale/create", SaleController.createSale);

export default router;
