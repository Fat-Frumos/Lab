import {Certificate} from "./Certificate";

export interface User {
  username: string;
  password: string;
  access_token: string;
  refresh_token: string;
  expired_at: string;
  certificates: Certificate[];
}
