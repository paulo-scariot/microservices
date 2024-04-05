import UserService from "../service/UserService.js";

class UserController {

  async findById(req, res) {
    let user = await UserService.findById(req);
    return res.status(user.status).json(user);
  }

  async findByEmail(req, res) {
    let user = await UserService.findByEmail();
    return res.status(user.status).json(user);
  }

  async getAcessToken(req, res) {
    let user = await UserService.getAcessToken(req);
    return res.status(user.status).json(user);
  }
}

export default new UserController();