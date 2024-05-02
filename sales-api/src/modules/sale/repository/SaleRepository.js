import Sale from "../model/Sale.js";

class SaleRepository {
  async save(sale) {
    try {
      return await Sale.create(sale);
    } catch (error) {
      console.error(error.message);
      return null;
    }
  }

  async findById(id) {
    try {
      return await Sale.findById(id);
    } catch (error) {
      console.error(error.message);
      return null;
    }
  }


  async findAll() {
    try {
      return await Sale.find();
    } catch (error) {
      console.error(error.message);
      return null;
    }
  }

  async findByProductId(productId) {
    try {
      return await Sale.find({ "products.productId": Number(productId) });
    } catch (error) {
      console.error(error.message);
      return null;
    }
  }
}

export default new SaleRepository();