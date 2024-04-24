import type { OrderStatus } from '../OrderStatus';
import type { LocationInternal } from './LocationInternal';
import type { OrderHandle } from './OrderHandle';

export type GeotagData = {
  orderHandle: OrderHandle;
  orderStatus: OrderStatus;
  data: Object;
  expectedLocation?: LocationInternal;
};
