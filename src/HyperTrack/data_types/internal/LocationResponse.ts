import type { Location } from '../Location';
import type { LocationError } from '../LocationError';

export type LocationSuccess = {
  type: 'success';
  value: Location;
};

export type LocationFailure = {
  type: 'failure';
  value: LocationError;
};

export type LocationResponse = LocationSuccess | LocationFailure;
