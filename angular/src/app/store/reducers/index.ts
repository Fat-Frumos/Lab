import {certificateReducer} from "./certificate.reducer";
import {ICertificate} from "../../model/entity/ICertificate";
import {cartReducer} from "./cart.reducer";
import {EntityState} from "@ngrx/entity";

export interface IState {
  certificates: {
    items: ICertificate[],
    loading: boolean
  };
  cart: EntityState<ICertificate>
}

export const reducers = {
  certificates: certificateReducer,
  cart: cartReducer
}
