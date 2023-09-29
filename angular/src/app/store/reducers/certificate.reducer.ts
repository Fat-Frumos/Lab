import {Action, createReducer, on} from "@ngrx/store";
import {ICertificate} from "../../model/entity/ICertificate";
import {
  getCertificatesPending,
  getCertificatesSuccess
} from "../actions/certificate.action";

const initialState: { items: ICertificate[], loading: boolean } = {
  items: [], loading: false
};

export const certificateReducer = createReducer(
  initialState,
  on(getCertificatesSuccess, (_state, action) => {
    return {
      items: action.certificates,
      loading: false
    };
  }),
  on(getCertificatesPending, (_state) => {
    return {
      ..._state,
      loading: true
    };
  }),
);

export default function reducer(state: any, action: Action) {
  return certificateReducer(state, action)
}
