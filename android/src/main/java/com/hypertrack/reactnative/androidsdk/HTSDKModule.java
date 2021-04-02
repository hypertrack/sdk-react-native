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
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@ReactModule(name = HTSDKModule.NAME)
public class HTSDKModule extends ReactContextBaseJavaModule {

    private static final String TAG = "HTSDKModule";

    public static final String NAME = "HyperTrack";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ACCURACY = "accuracy";

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
            Log.e(TAG, "Hypertrack SDK initialization failed.",  exception);
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
    public void addGeotag(ReadableMap rMap, ReadableMap expectedLocation, Boolean isRestricted, final Promise promise) {
        if ( isRestricted == Boolean.TRUE ) {
            if (expectedLocation == null) {
                // 3 is invalid arguments. check js/CriticalErrors.js
                fail(3, promise);
                return;
            }
            double deviation = expectedLocation.hasKey(ACCURACY)
                    ? expectedLocation.getDouble(ACCURACY)
                    : 100.0;
            Location expected = expectedLocationFromMap(expectedLocation);
            if (expected == null) {
                fail(3, promise);
                return;
            }
            try {
                Map<String,Serializable> tagData = new HashMap<>(rMap.toHashMap().size());
                for (Map.Entry<String, Object> entry : rMap.toHashMap().entrySet()) {
                    if (entry.getValue() instanceof Serializable) {
                        tagData.put(entry.getKey(), (Serializable) entry.getValue());
                    }
                }
                sdkInstance.addRestrictedGeotag(tagData, expected, Double.valueOf(deviation).intValue(),
                        new HyperTrack.ResultCallback<HyperTrack.Result>() {
                            @Override
                            public void onSuccess(HyperTrack.Result result) {
                                switch (result) {
                                    case SUCCESS:
                                        promise.resolve(null);
                                        break;
                                    case LOCATION_MISMATCH:
                                        fail(1, promise);
                                        break;
                                    case LOCATION_NOT_AVAILABLE:                                }
                                        fail(2, promise);
                            }
                        });
            } catch (Exception e) {
                String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
                promise.reject(error, e);
            }
        } else {
            Location expected = expectedLocationFromMap(expectedLocation);
            try {
                sdkInstance.addGeotag(rMap.toHashMap(), expected);
                promise.resolve(null);
            } catch (Exception e) {
                String error = String.format(Locale.ENGLISH, "%d", RN_ERROR_GENERAL);
                promise.reject(error, e);
            }
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
