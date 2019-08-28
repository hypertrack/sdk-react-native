package com.hypertrack.reactnative.androidsdk;

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
import com.hypertrack.sdk.Config;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;


@ReactModule(name = HTSDKModule.NAME)
public class HTSDKModule extends ReactContextBaseJavaModule {

    private static final String TAG = HTSDKModule.class.getSimpleName();

    public static final String NAME = "HyperTrack";

    public TrackingStateObserver.OnTrackingStateChangeListener trackingStateChangeListener;

    public HTSDKModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void initialize(String publishableKey, Boolean startsTracking) {
        if (getCurrentActivity() != null) {
            HyperTrack.removeTrackingStateListener(trackingStateChangeListener);
            trackingStateChangeListener = null;
            HyperTrack.initialize(getCurrentActivity(),
                    publishableKey,
                    new Config.Builder()
                            .enableAutoStartTracking(startsTracking)
                            .build());
        } else {
            Log.d(TAG, "Hypertrack SDK initialization failed while: CurrentActivity is null");
        }
    }

    @ReactMethod
    public void subscribeOnEvents() {
        if (trackingStateChangeListener == null) {
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
            HyperTrack.addTrackingStateListener(trackingStateChangeListener);
        }
    }

    @ReactMethod
    public void getDeviceID(Promise promise) {
        promise.resolve(HyperTrack.getDeviceId());
    }

    @ReactMethod
    public void isTracking(Promise promise) {
        promise.resolve(HyperTrack.isTracking());
    }


    @ReactMethod
    public void startTracking() {
        HyperTrack.startTracking();
    }

    @ReactMethod
    public void stopTracking() {
        HyperTrack.stopTracking();
    }

    @ReactMethod
    public void setTripMarker(ReadableMap rMap, Promise promise) {
        try {
            HyperTrack.tripMarker(rMap.toHashMap());
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject(TAG + "_ERROR", e);
        }
    }

    @ReactMethod
    public void setDevice(String name, ReadableMap rMap, Promise promise) {
        try {
            HyperTrack.setNameAndMetadataForDevice(name, rMap.toHashMap());
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject(TAG + "_ERROR", e);
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
            HyperTrack.enableMockLocation();
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
