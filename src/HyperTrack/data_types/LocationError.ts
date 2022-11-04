import type { HyperTrackError } from './HyperTrackError';

type NotRunning = {
  type: 'notRunning';
};
type Starting = {
  type: 'starting';
};
export type Errors = {
  type: 'errors';
  errors: HyperTrackError[];
};

export type LocationError = NotRunning | Starting | Errors;
