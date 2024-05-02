import SaleService from "../service/SaleService.js"

class SaleController {
  async createSale(req, res) {
    let sale = await SaleService.createSale(req);
    return res.status(sale.status).json(sale);
  }

  async findById(req, res) {
    let sale = await SaleService.findById(req);
    return res.status(sale.status).json(sale);
  }

  async findAll(req, res) {
    let sales = await SaleService.findAll(req);
    return res.status(sales.status).json(sales);
  }

  async findByProductId(req, res) {
    let sale = await SaleService.findByProductId(req);
    return res.status(sale.status).json(sale);
  }
}

export default new SaleController();