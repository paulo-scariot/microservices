import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import UserException from "../exception/UserException.js";
import * as secrets from "../../../config/constants/secrets.js";

class UserService {

	async findById(req) {
		try {
			const {id} = req.params;
			const {authUser} = req;
			this.validateRequestDataId(id);
			let user = await UserRepository.findById(id);
			this.validateUserNotFound(user);
      this.validateAuthenticatedUser(user, authUser);
			return {
				status: httpStatus.SUCCESS,
				user: {
					id: user.id,
					name: user.name,
					email: user.email
				}
			}
		} catch (error) {
			return {
				status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
				message: error.message
			}
		}
	}

	async findByEmail(req) {
		try {
			const {email} = req.params;
			const {authUser} = req;
			this.validateRequestDataEmail(email);
			let user = await UserRepository.findByEmail(email);
			this.validateUserNotFound(user, authUser);
      this.validateAuthenticatedUser(user, authUser);
			return {
				status: httpStatus.SUCCESS,
				user: {
					id: user.id,
					name: user.name,
					email: user.email
				}
			}
		} catch (error) {
			return {
				status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
				message: error.message
			}
		}
	}

	async getAcessToken(req) {
		try {
			const {email, password} = req.body;
			this.validateAcessTokenData(email, password);
			let user = await UserRepository.findByEmail(email);
			this.validateUserNotFound(user);
      await this.validatePassword(password, user.password);
      const authUser = {id: user.id, name: user.name, email: user.name};
      const accessToken = jwt.sign({authUser}, secrets.API_SECRET, {
        expiresIn: "1d"
      });
			return {
				status: httpStatus.SUCCESS,
				accessToken
			}
		} catch (error) {
			return {
				status: error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
				message: error.message
			}
		}
	}

  validateRequestDataId(id) {
		if (!id){
			throw new UserException(httpStatus.BAD_REQUEST, "User id was not informed");
		}
	}

	validateRequestDataEmail(email) {
		if (!email){
			throw new UserException(httpStatus.BAD_REQUEST, "User email was not informed");
		}
	}

	validateAcessTokenData(email, password) {
		if (!email || !password){
			throw new UserException(httpStatus.UNAUTHORIZED, "Email and password must be informed");
		}
	}

  async validatePassword(password, hashPassword){
    if (!await bcrypt.compare(password, hashPassword)) {
      throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match")
    }
  }

  validateAuthenticatedUser(user, authUser){
    if (!authUser || user.id !== authUser.id) {
      throw new UserException(httpStatus.FORBIDDEN, "You cannot see this user data");
    }
  }

	validateUserNotFound(user){
		if (!user) {
			throw new UserException(httpStatus.BAD_REQUEST, "User not found")
		}
	}
}

export default new UserService();