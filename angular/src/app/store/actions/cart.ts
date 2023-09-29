import {createAction, props} from "@ngrx/store";
import {ICartProduct} from "../../interfaces/ICartProduct";

export const addProductToCart = createAction(
  '[Cart] Add product to cart',
  props<{ product: ICartProduct }>()
);

export const removeFromProductToCart = createAction(
  '[Cart] Remove from product to cart',
  props<{ id: string }>()
);

export const incrementProductInCart = createAction(
  '[Cart] Increment product in cart',
  props<{ id: string }>()
);

export const decrementProductInCart = createAction(
  '[Cart] Decrement product in cart',
  props<{ id: string }>()
);
