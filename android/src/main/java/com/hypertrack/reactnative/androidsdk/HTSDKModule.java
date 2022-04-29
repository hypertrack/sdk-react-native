package com.hypertrack.reactnative.androidsdk;

import android.location.Location;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hypertrack.sdk.AsyncResultReceiver;
import com.hypertrack.sdk.GeotagResult;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.OutageReason;
import com.hypertrack.sdk.Result;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;

import java.util.Locale;


@ReactModule(name = HTSDKModule.NAME)
public class HTSDKModule extends ReactContextBaseJavaModule {

    public TrackingStateObserver.OnTrackingStateChangeListener trackingStateChangeListener;
    public HyperTrack sdkInstance;

    public HTSDKModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void initialize(String publishableKey, Boolean startsTracking, Boolean automaticallyRequestPermissions, Promise promise) {
        try {
            sdkInstance = HyperTrack.getInstance(
                    publishableKey
            ).backgroundTrackingRequirement(false);
            if (startsTracking) {
                sdkInstance.start();
            }
            promise.resolve(true);
        } catch (java.lang.Exception exception) {
            Log.e(TAG, "Hypertrack SDK initialization failed.", exception);
            String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_INVALID_PUBLISHABLE_KEY);
            promise.reject(error, new Exception("Hypertrack SDK initialization failed."));
        }
    }

    @ReactMethod
    public void subscribeOnEvents() {
        if (trackingStateChangeListener != null) {
            sdkInstance.removeTrackingListener(trackingStateChangeListener);
            trackingStateChangeListener = null;
        }
        trackingStateChangeListener = new TrackingStateObserver.OnTrackingStateChangeListener() {
            @Override
            public void onError(TrackingError trackingError) {
                WritableMap params = Arguments.createMap();
                params.putInt("code", convertToRNErrorsCode(trackingError.code));
                params.putString("message", trackingError.message);
                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTrackingErrorHyperTrack", params);
            }

            @Override
            public void onTrackingStart() {
                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTrackingStartHyperTrack", null);
            }

            @Override
            public void onTrackingStop() {
                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTrackingStopHyperTrack", null);
            }
        };
        sdkInstance.addTrackingListener(trackingStateChangeListener);
    }

    @ReactMethod
    public void getDeviceID(Promise promise) {
        promise.resolve(sdkInstance.getDeviceID());
    }

    @ReactMethod
    public void isTracking(Promise promise) {
        promise.resolve(sdkInstance.isRunning());
    }


    @ReactMethod
    public void startTracking() {
        sdkInstance.start();
    }

    @ReactMethod
    public void stopTracking() {
        sdkInstance.stop();
    }

    @ReactMethod
    public void syncDeviceSettings() {
        sdkInstance.syncDeviceSettings();
    }

    @ReactMethod
    public void setTripMarker(ReadableMap rMap, Promise promise) {
        try {
            sdkInstance.addGeotag(rMap.toHashMap());
            promise.resolve(null);
        } catch (Exception e) {
            String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
            promise.reject(error, e);
        }
    }

    @ReactMethod
    public void addGeotag(ReadableMap rMap, ReadableMap expectedLocation, final Promise promise) {
        try {
            Location expected = expectedLocationFromMap(expectedLocation);
            GeotagResult result = sdkInstance.addGeotag(rMap.toHashMap(), expected);
            if (result instanceof GeotagResult.Success) {
                Location deviceLocation = ((GeotagResult.Success) result).getDeviceLocation();
                WritableMap map = Arguments.createMap();
                map.putDouble("latitude", deviceLocation.getLatitude());
                map.putDouble("longitude", deviceLocation.getLongitude());
                promise.resolve(map);

            } else {
                String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
                promise.reject(error);
            }
        } catch (Exception e) {
            String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
            promise.reject(error, e);
        }
    }

    private Location expectedLocationFromMap(ReadableMap expectedLocation) {
        if (!expectedLocation.hasKey(LATITUDE) || !expectedLocation.hasKey(LONGITUDE)) return null;
        try {
            double latitude = expectedLocation.getDouble(LATITUDE);
            double longitude = expectedLocation.getDouble(LONGITUDE);
            if (longitude > 180.0 || longitude < -180.0) return null;
            if (latitude > 90.0 || latitude < -90.0) return null;
            Location expected = new Location("any");
            expected.setLatitude(latitude);
            expected.setLongitude(longitude);
            return expected;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private void fail(int error, Promise promise) {
        WritableMap map = Arguments.createMap();
        map.putInt("code", error);
        promise.resolve(map);
    }

    @ReactMethod
    public void setDeviceName(String name, Promise promise) {
        try {
            sdkInstance.setDeviceName(name);
            promise.resolve(true);
        } catch (Exception e) {
            String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
            promise.reject(error, e);
        }
    }

    @ReactMethod
    public void setMetadata(ReadableMap rMap, Promise promise) {
        try {
            sdkInstance.setDeviceMetadata(rMap.toHashMap());
            promise.resolve(true);
        } catch (Exception e) {
            String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
            promise.reject(error, e);
        }
    }

    @ReactMethod
    public void enableDebugLogging(Boolean enable) {
        if (enable) {
            HyperTrack.enableDebugLogging();
        }
    }

    @ReactMethod
    public void enableMockLocation(Boolean enable) {
        if (enable) {
            sdkInstance.allowMockLocations();
        }
    }

    @ReactMethod
    public void getLocation(Promise promise) {
        promise.resolve(locationResultToMap(sdkInstance.getLatestLocation()));
    }

    @ReactMethod
    public void getCurrentLocation(final Promise promise) {
        sdkInstance.getCurrentLocation(new AsyncResultReceiver<Location, OutageReason>() {
            @Override
            public void onResult(Result<Location, OutageReason> result) {
                promise.resolve(locationResultToMap(result));
            }
        });
    }

    private WritableMap locationResultToMap(Result<Location, OutageReason> result) {
        WritableMap resultMap = Arguments.createMap();
        if (result.isSuccess()) {
            Location location = result.getValue();
            WritableMap locationMap = Arguments.createMap();
            locationMap.putDouble(LATITUDE, location.getLatitude());
            locationMap.putDouble(LONGITUDE, location.getLongitude());
            resultMap.putMap(KEY_LOCATION, locationMap);
        } else {
            OutageReason outage = result.getError();
            WritableMap errorMap = Arguments.createMap();
            errorMap.putString(KEY_OUTAGE_CODE, getOutageName(outage));
            resultMap.putMap(KEY_ERROR, errorMap);
        }
        return resultMap;
    }

    private String getOutageName(OutageReason outageReason) {
        switch (outageReason) {
            case MISSING_LOCATION_PERMISSION:
                return OUTAGE_CODE_LOCATION_PERMISSIONS_DENIED;

            case MISSING_ACTIVITY_PERMISSION:
                return OUTAGE_CODE_MOTION_ACTIVITY_PERMISSIONS_DENIED;

            case LOCATION_SERVICE_DISABLED:
                return OUTAGE_CODE_LOCATION_SERVICES_DISABLED;

            case NOT_TRACKING:
                return OUTAGE_CODE_NOT_RUNNING;

            case START_HAS_NOT_FINISHED:
                return OUTAGE_CODE_STARTING;

            case NO_GPS_SIGNAL:
            case RESTART_REQUIRED:
            default:
                return OUTAGE_CODE_GPS_SIGNAL_LOST;
        }
    }

    private static final String TAG = "HTSDKModule";

    private static final String KEY_LOCATION = "location";
    private static final String KEY_ERROR = "error";
    private static final String KEY_OUTAGE_CODE = "code";

    public static final String NAME = "HyperTrack";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ACCURACY = "accuracy";

    private static final String OUTAGE_CODE_LOCATION_PERMISSIONS_DENIED = "location_permissions_denied";
    private static final String OUTAGE_CODE_LOCATION_SERVICES_DISABLED = "location_services_disabled";
    private static final String OUTAGE_CODE_MOTION_ACTIVITY_PERMISSIONS_DENIED = "motion_activity_permissions_denied";
    private static final String OUTAGE_CODE_NOT_RUNNING = "not_running";
    private static final String OUTAGE_CODE_GPS_SIGNAL_LOST = "gps_signal_lost";
    private static final String OUTAGE_CODE_STARTING = "starting";

    private static final int RN_ERROR_GENERAL = 0;
    private static final int RN_ERROR_INVALID_PUBLISHABLE_KEY = 1;
    private static final int RN_ERROR_AUTHORIZATION_FAILED = 2;
    private static final int RN_ERROR_PERMISSION_DENIED = 3;

    private int convertToRNErrorsCode(int androidSdkCode) {
        switch (androidSdkCode) {
            case TrackingError.INVALID_PUBLISHABLE_KEY_ERROR:
                return RN_ERROR_INVALID_PUBLISHABLE_KEY;
            case TrackingError.AUTHORIZATION_ERROR:
                return RN_ERROR_AUTHORIZATION_FAILED;
            case TrackingError.PERMISSION_DENIED_ERROR:
                return RN_ERROR_PERMISSION_DENIED;
            default:
                return RN_ERROR_GENERAL;
        }
    }

}
