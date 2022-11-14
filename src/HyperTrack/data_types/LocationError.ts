import type { HyperTrackError } from './HyperTrackError';

export type NotRunning = {
  type: 'notRunning';
};
export type Starting = {
  type: 'starting';
};
export type Errors = {
  type: 'errors';
  errors: HyperTrackError[];
};

export type LocationError = NotRunning | Starting | Errors;
