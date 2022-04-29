/**
 * An error that we couldn't recognize.
 */
export const GENERAL = 0;
/**
 * Publishable key is invalid. Check your key on hypertrack dashboard setup.
 */
export const INVALID_PUBLISHABLE_KEY = 1;
/**
 * Your free trial ended or account was not renewed. Check your account on hypertrack dashboard.
 */
export const AUTHORIZATION_FAILED = 2;
/**
 * Tracking won't start due to denied (or absent) permission.
 */
export const PERMISSION_DENIED = 3;


/**
 * A base interface for errors in SDK.
 */
export type Error = {
    /**
     * Code of SDK error from {@link GENERAL}, {@link INVALID_PUBLISHABLE_KEY}, {@link AUTHORIZATION_FAILED}, {@link PERMISSION_DENIED}.
     */
    code: number,
    /**
     * Message with additional information. Optional.
     */
    message: ?string
};

// Geotags callback

/** Expected location is not supported on this platform */
export const PLATFORM_NOT_SUPPORTED = 0;
/** Expected vs actual location difference is greater then required */
export const LOCATION_MISMATCH = 1;
/** SDK can't get actual location from OS sensors */
export const LOCATION_NOT_AVAILABLE = 2;
/** Invalid arguments  */
export const INVALID_ARGUMENTS = 3;

/** 
 * Interface for accessing adding geotag result. 
 * @property {number} code - is one of {@link PLATFORM_NOT_SUPPORTED}, {@link LOCATION_NOT_AVAILABLE}, {@link LOCATION_MISMATCH}, {@link INVALID_ARGUMENTS}
 */
export type GeotagError = { code: number }



// The reason for why the SDK doesn't have a fresh location.

// The user has not chosen whether the app can use location services.
// SDK automatically asked for permissions.
export const LOCATION_PERMISSIONS_NOT_DETERMINED = "location_permissions_not_determined";
// The user has not chosen whether the app can use location services.
// SDK can't ask for permissions in background.
export const LOCATION_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = "location_permissions_cant_be_asked_in_background";
// Can't start tracking in background with When In Use location
// permissions. SDK will automatically start tracking when app will return
// to foreground.
export const LOCATION_PERMISSIONS_INSUFFICIENT_FOR_BACKGROUND = "location_permissions_insufficient_for_background";
// The app is not authorized to use location services.
export const LOCATION_PERMISSIONS_RESTRICTED = "location_permissions_restricted";
// The user didn't grant precise location permissions or downgraded permissions to imprecise.
export const LOCATION_PERMISSIONS_REDUCED_ACCURACY = "location_permissions_reduced_accuracy";
// The user denied location permissions.
export const LOCATION_PERMISSIONS_DENIED = "location_permissions_denied";
// The user disabled location services systemwide.
export const LOCATION_SERVICES_DISABLED = "location_services_disabled";
// The user has not chosen whether the app can use motion activity
// services. SDK automatically asked for permissions.
export const MOTION_ACTIVITY_PERMISSIONS_NOT_DETERMINED = "motion_activity_permissions_not_determined";
// The user has not chosen whether the app can use motion activity
// services. SDK can't ask for permissions in background.
export const MOTION_ACTIVITY_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND = "motion_activity_permissions_cant_be_asked_in_background";
// Motion activity permissions denied after SDK's initialization. Granting
// them will restart the app, so in effect, they are denied during this app's
// session.
export const MOTION_ACTIVITY_PERMISSIONS_DENIED = "motion_activity_permissions_denied";
// The user disabled motion services systemwide.
export const MOTION_ACTIVITY_SERVICES_DISABLED = "motion_activity_services_disabled";
// The SDK is not collecting locations because it's either not tracking or is unavailable.
export const NOT_RUNNING = "not_running";
// GPS satellites are not in view.
export const GPS_SIGNAL_LOST = "gps_signal_lost";
// The SDK started collecting locations and is waiting for OS location services to respond.
export const STARTING = "starting";
// The user enabled mock location app while mocking locations is prohibited.
export const LOCATION_MOCKED = "location_mocked";

/** 
 * Interface for accessing location error value. 
 * @property {string} code - reason for why location is not available, one of {@link LOCATION_PERMISSIONS_NOT_DETERMINED}, {@link LOCATION_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND}, {@link LOCATION_PERMISSIONS_INSUFFICIENT_FOR_BACKGROUND}, {@link LOCATION_PERMISSIONS_RESTRICTED}, {@link LOCATION_PERMISSIONS_REDUCED_ACCURACY}, {@link LOCATION_SERVICES_DISABLED}, {@link MOTION_ACTIVITY_PERMISSIONS_NOT_DETERMINED}, {@link MOTION_ACTIVITY_PERMISSIONS_CANT_BE_ASKED_IN_BACKGROUND}, {@link MOTION_ACTIVITY_PERMISSIONS_DENIED}, {@link MOTION_ACTIVITY_SERVICES_DISABLED}, {@link GPS_SIGNAL_LOST}, {@link STARTING}
 */
export type LocationError = { code: string }
