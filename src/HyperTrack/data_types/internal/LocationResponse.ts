import type { Location } from '../Location';
import type { LocationErrorInternal } from './LocationErrorInternal';

export type LocationSuccess = {
  type: 'success';
  value: Location;
};

export type LocationFailure = {
  type: 'failure';
  value: LocationErrorInternal;
};

export type LocationResponse = LocationSuccess | LocationFailure;
