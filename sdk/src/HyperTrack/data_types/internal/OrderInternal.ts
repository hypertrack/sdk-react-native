import type { Result } from 'lib/typescript';
import type { LocationError } from '../LocationError';
import type { LocationErrorInternal } from './LocationErrorInternal';

export type OrderInternal = {
  orderHandle: string;
  isInsideGeofence: Result<boolean, LocationErrorInternal>;
  index: number;
};
