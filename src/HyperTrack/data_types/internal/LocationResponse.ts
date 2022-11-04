import type { Location } from '../Location';
import type { LocationError } from '../LocationError';

type LocationSuccess = {
  type: 'success';
  value: Location;
};

type LocationFailure = {
  type: 'failure';
  value: LocationError;
};

export type LocationResponse = LocationSuccess | LocationFailure;
