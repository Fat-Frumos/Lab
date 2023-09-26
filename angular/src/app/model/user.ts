import {IUser} from "./entity/IUser";
import {ICertificate} from "./entity/ICertificate";
import {LoginState} from "./enum/LoginState";

export class User implements IUser {
  access_token!: string;
  certificates: ICertificate[] = [];
  expired_at!: string;
  password!: string;
  refresh_token!: string;
  state: LoginState = LoginState.GUEST;
  username: string = 'user';
}
