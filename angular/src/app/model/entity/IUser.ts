import {ICertificate} from "./ICertificate";
import {LoginState} from "../enum/LoginState";

export interface IUser {
  username: string;
  password: string;
  access_token: string;
  refresh_token: string;
  expired_at: string;
  certificates: ICertificate[];
  state: LoginState;
}
