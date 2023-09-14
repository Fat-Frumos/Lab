import {Certificate} from "../model/Certificate";

export interface IMapper {
  mapper(data: any): Certificate;
}
