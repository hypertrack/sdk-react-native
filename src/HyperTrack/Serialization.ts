import type {
  HyperTrackError,
  Location,
  LocationError,
  SDKDeviceID,
  SDKErrors,
  SDKIsAvailable,
  SDKIsTracking,
  SDKLocation,
  SDKLocationError,
} from 'src/types';

export function deserializeDeviceID(deviceIDResponse: SDKDeviceID): string {
  if (deviceIDResponse.type !== 'deviceID') {
    throw new Error('Unexpected response type');
  }
  return deviceIDResponse.value;
}

export function deserializeAvailability(isAvailable: SDKIsAvailable): boolean {
  if (isAvailable.type !== 'isAvailable') {
    throw new Error('Unexpected response type');
  }
  return isAvailable.value;
}

export function deserializeIsTracking(isTracking: SDKIsTracking): boolean {
  if (isTracking.type !== 'isTracking') {
    throw new Error('Unexpected response type');
  }
  return isTracking.value;
}

export function deserializeLocationResponse(
  locationResponse: SDKLocationError | SDKLocation
): LocationError | Location {
  if (locationResponse.type === 'notRunning') {
    return locationResponse.type;
  }
  if (locationResponse.type === 'starting') {
    return locationResponse.type;
  }
  if (locationResponse.type === 'errors') {
    return locationResponse.errors;
  }
  if (locationResponse.type === 'location') {
    return {
      latitude: locationResponse.value.latitude,
      longitude: locationResponse.value.longitude,
    };
  }
  throw new Error('Unexpected response type');
}

export function deserializeHyperTrackErrorsResponse(
  errorsResponse: SDKErrors
): HyperTrackError[] {
  if (errorsResponse.type !== 'errors') {
    throw new Error('Unexpected response type');
  }
  return errorsResponse.errors;
}
