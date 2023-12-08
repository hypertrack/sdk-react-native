import type { LocationInternal } from './LocationInternal';

export type LocationWithDeviationInternal = {
  type: 'locationWithDeviation';
  value: {
    location: LocationInternal;
    deviation: number;
  };
};
