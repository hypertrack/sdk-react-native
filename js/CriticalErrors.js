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