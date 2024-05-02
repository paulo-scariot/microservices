import SaleRepository from "../repository/SaleRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { PENDING } from "../model/SaleStatus.js";
import SaleException from "../exception/SaleException.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import ProductClient from "../../product/client/ProductClient.js";

class SaleService {
  async createSale(req) {
    try {
      let saleData = req.body;
      this.validateSaleData(saleData);
      const { authUser } = req;
      const { authorization } = req.headers;
      let sale = {
        status: PENDING,
        user: authUser,
        createdAt: new Date(),
        updatedAt: new Date(),
        products: saleData.products
      }
      await this.validateProductStock(sale, authorization);
      let createdSale = await SaleRepository.save(sale);
      this.sendMessage(createdSale);
      return {
        status: httpStatus.SUCCESS,
        createdSale
      }
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message
      }
    }
  }

  async updateSale(saleMessage) {
    try {
      const sale = JSON.parse(saleMessage);
      if (sale.status && sale.saleId) {
        let existingSale = await SaleRepository.findById(sale.saleId);
        if (existingSale && sale.status !== existingSale.status) {
          existingSale.status = sale.status;
          existingSale.updatedAt = new Date();
          await SaleRepository.save(existingSale);
        }
      } else {
        console.warn("the sale message was not complete.");
      }

    } catch (error) {
      console.error("could not parse sale message from queue.");
      console.error(error.message);
    }
  }

  validateSaleData(data) {
    if (!data || !data.products) {
      throw new SaleException(httpStatus.BAD_REQUEST, "the products must be informed.")
    }
  }

  async validateProductStock(sale, token) {
    let response = await ProductClient.checkProductStock(sale.products, token)
      .catch(error => {
        throw new SaleException(error.response.status, error.response.data.message);
      });
    if (response !== true) {
      throw new SaleException(httpStatus.BAD_REQUEST, "the stock is out of products.")
    }
  }

  sendMessage(createdSale) {
    const message = {
      saleId: createdSale.id,
      products: createdSale.products
    }
    sendMessageToProductStockUpdateQueue(message);
  }

  async findById(req) {
    try {
      const {id} = req.params;
      this.validateInformedId(id);
      const existingSale = await SaleRepository.findById(id);
      if (!existingSale) {
        throw new SaleException(httpStatus.BAD_REQUEST, "the sale was not found");
      }
      return {
        status: httpStatus.SUCCESS,
        existingSale
      }
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message
      }
    }
  }

  validateInformedId(id) {
    if(!id){
      throw new SaleException(httpStatus.BAD_REQUEST, "the order ID must be informed");
    }
  }

  async findAll(req) {
    try {
      const existingSales = await SaleRepository.findAll();
      return {
        status: httpStatus.SUCCESS,
        existingSales
      }
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message
      }
    }
  }

  async findByProductId(req){
    try {
      const {productId} = req.params;
      this.validateInformedId(productId);
      const existingSales = await SaleRepository.findByProductId(productId);
      return {
        status: httpStatus.SUCCESS,
        salesIds: existingSales.map(sale => sale.id)
      }
    } catch (error) {
      return {
        status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: error.message
      }
    }
  }
}

export default new SaleService(httpStatus.BAD_REQUEST, 'The product must be informed.');