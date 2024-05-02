import Sale from "../../modules/sale/model/Sale.js";

export async function createInitialData(){
    await Sale.collection.drop();
    await Sale.create({
        products: [
            {
                productId: 2,
                quantity: 3
            }
        ],
        user: {
            id: 'hgsiudhgiusdh',
            name: "User test",
            email: "user@gmail.com"
        },
        status: 'APPROVED',
        createdAt: new Date(),
        updatedAt: new Date(),
    });
    let initialData = await Sale.find();
    console.log(`initial data was created: ${JSON.stringify(initialData, undefined, 4)}`);
}