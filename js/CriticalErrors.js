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