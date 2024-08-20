import type { Result } from './Result';
import type { LocationError } from './LocationError';

export type Order = {
  orderHandle: string;
  isInsideGeofence: Result<boolean, LocationError>;
};
