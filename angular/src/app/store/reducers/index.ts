import {certificateReducer} from "./certificate.reducer";
import {ICertificate} from "../../model/entity/ICertificate";
import {cartReducer} from "./cart.reducer";
import {EntityState} from "@ngrx/entity";
import {ICartProduct} from "../../interfaces/ICartProduct";

export interface IState {
  certificates: {
    items: ICertificate[],
    loading: boolean
  };
  cart: EntityState<ICartProduct>
}

export const reducers = {
  certificates: certificateReducer,
  cart: cartReducer
}
