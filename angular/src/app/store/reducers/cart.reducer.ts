import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {ICartProduct} from "../../interfaces/ICartProduct";
import {
  Action,
  createFeatureSelector,
  createReducer,
  createSelector,
  on
} from "@ngrx/store";
import {
  addProductToCart,
  decrementProductInCart,
  incrementProductInCart,
  removeFromProductToCart
} from "../actions/cart";
import {ActionReducer} from "@ngrx/store/src/models";

export const cartAdapter: EntityAdapter<ICartProduct> = createEntityAdapter({
  selectId: (product: ICartProduct) => product.id
})

const initialState: EntityState<ICartProduct> = cartAdapter.getInitialState();

const reducer: ActionReducer<EntityState<ICartProduct>> = createReducer(initialState,
  on(addProductToCart, (state: EntityState<ICartProduct>, {product}) => {
    const entity: ICartProduct = state.entities[product.id] as ICartProduct;
    return cartAdapter.upsertOne({
      ...product,
      count: entity ? ++entity.count : 1
    }, state);
  }),
  on(removeFromProductToCart, (state: EntityState<ICartProduct>, {id}) => {
    return cartAdapter.removeOne(id, state);
  }),
  on(incrementProductInCart, (state: EntityState<ICartProduct>, {id}) => {
    const entity: ICartProduct = state.entities[id] as ICartProduct;
    return cartAdapter.updateOne({id, changes: {count: ++entity.count}}, state);
  }),
  on(decrementProductInCart, (state: EntityState<ICartProduct>, {id}) => {
    const entity: ICartProduct = state.entities[id] as ICartProduct;
    return cartAdapter.updateOne({id, changes: {count: --entity.count}}, state);
  }),
)

export function cartReducer(state: EntityState<ICartProduct> | undefined, action: Action): EntityState<ICartProduct> {
  return reducer(state, action);
}

export const {selectAll} = cartAdapter.getSelectors();
export const selectCart = createFeatureSelector<EntityState<ICartProduct>>('cart');
export const selectProductInCard = createSelector(selectCart, selectAll);

