import axios from "axios";
import { PRODUCT_API_URL } from "../../../config/constants/secrets.js";

class ProductClient {
	async checkProductStock(products, token) {
		const headers = {
			Authorization: `${token}`
		}
		console.info(`sending request to Product API with data: ${JSON.stringify(products)}`);
		let response = false;
		await axios.post(`${PRODUCT_API_URL}/checkstock`, { products }, { headers })
			.then(res => {
				response = true;
			})
			.catch(error => {
				throw error;
			});
		return response;
	}

}

export default new ProductClient();