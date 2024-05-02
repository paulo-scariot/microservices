const env = process.env;

const MONGO_USER = env.MONGO_USER ? env.MONGO_USER : "admin";
const MONGO_PASS = env.MONGO_PASS ? env.MONGO_PASS : "1234";
const MONGO_DB_URL = env.MONGO_DB_URL ? env.MONGO_DB_URL : "mongodb://localhost:27017/sales";
const API_SECRET = env.API_SECRET ? env.API_SECRET : "YXV0aC1hcGktc2VjcmV0LWRldi0xMjM0NTY=";
const RABBIT_MQ_URL = env.RABBIT_MQ_URL ? env.RABBIT_MQ_URL : "amqp://localhost:5672"
const PRODUCT_API_URL = env.PRODUCT_API_URL ? env.PRODUCT_API_URL : "http://localhost:8081/api/product";

export { MONGO_USER, MONGO_PASS, MONGO_DB_URL, API_SECRET, RABBIT_MQ_URL, PRODUCT_API_URL}