import type { HyperTrackError } from './HyperTrackError';
import type { NotRunning, Starting } from './internal/LocationErrorInternal';

export type Errors = {
  type: 'errors';
  value: HyperTrackError[];
};

export type LocationError = NotRunning | Starting | Errors;
