import type { Result } from '../Result';
import type { LocationErrorInternal } from './LocationErrorInternal';

export type OrderInternal = {
  orderHandle: string;
  isInsideGeofence: Result<boolean, LocationErrorInternal>;
  index: number;
};
