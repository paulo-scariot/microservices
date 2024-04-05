import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export async function createInicialData() {
  try {
    await User.sync({ force: true });

    let password = await bcrypt.hash("1234", 10);

    await User.create({
      name: "user 1",
      email: "user1@teste.com",
      password: password
    });

    await User.create({
      name: "user 2",
      email: "user2@teste.com",
      password: password
    });
  } catch (error){
    console.log(error);
  }

}