import type { HyperTrackErrorInternal } from './HyperTrackErrorInternal';

export type NotRunning = {
  type: 'notRunning';
};
export type Starting = {
  type: 'starting';
};
export type ErrorsInternal = {
  type: 'errors';
  value: HyperTrackErrorInternal[];
};

export type LocationErrorInternal = NotRunning | Starting | ErrorsInternal;
