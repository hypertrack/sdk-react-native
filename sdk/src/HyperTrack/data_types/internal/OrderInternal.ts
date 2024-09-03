import type { Result } from '../Result';
import type { IsInsideGeofence } from './IsInsideGeofence';
import type { LocationErrorInternal } from './LocationErrorInternal';

export type OrderInternal = {
  orderHandle: string;
  isInsideGeofence: Result<IsInsideGeofence, LocationErrorInternal>;
  index: number;
};
