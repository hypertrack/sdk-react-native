package com.hypertrack.reactnative.androidsdk;

import android.text.TextUtils;
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
import com.google.gson.reflect.TypeToken;
import com.hypertrack.sdk.Config;
import com.hypertrack.sdk.CoreSDKState;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;
import com.hypertrack.sdk.persistence.DataStore;
import com.hypertrack.sdk.utils.StaticUtilsAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;


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
    public void initialize(String publishableKey, Boolean startsTracking, Promise promise) {
        if (getReactApplicationContext().getApplicationContext() != null) {
            HyperTrack.initialize(getReactApplicationContext().getApplicationContext(),
                    publishableKey,
                    new Config.Builder()
                            .enableAutoStartTracking(startsTracking)
                            .build());
            promise.resolve(true);
        } else {
            Log.d(TAG, "Hypertrack SDK initialization failed while: ApplicationContext is null");
            promise.reject(TAG + "_ERROR", new Exception("Hypertrack SDK initialization failed while: ApplicationContext is null"));
        }
    }

    @ReactMethod
    public void subscribeOnEvents() {
        if (trackingStateChangeListener != null) {
            HyperTrack.removeTrackingStateListener(trackingStateChangeListener);
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
        HyperTrack.addTrackingStateListener(trackingStateChangeListener);
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
    public void syncDeviceSettings() {
        HyperTrack.syncDeviceSettings();
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
    public void setDeviceName(String name, Promise promise) {
        try {
            Map<String, Object> metaData = Collections.emptyMap();
            CoreSDKState sCoreSDKState =
                    CoreSDKState.getInstance(new DataStore(getReactApplicationContext().getApplicationContext()));
            String metaDataString = sCoreSDKState.getMetaData();
            if (!TextUtils.isEmpty(metaDataString)) {
                Type empMapType = new TypeToken<Map<String, Object>>() {}.getType();
                metaData = StaticUtilsAdapter.getGson().fromJson(metaDataString, empMapType);
            }
            HyperTrack.setNameAndMetadataForDevice(name, metaData);
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(TAG + "_ERROR", e);
        }
    }

    @ReactMethod
    public void setMetadata(ReadableMap rMap, Promise promise) {
        try {
            CoreSDKState sCoreSDKState =
                    CoreSDKState.getInstance(new DataStore(getReactApplicationContext().getApplicationContext()));
            String name = sCoreSDKState.getName();
            HyperTrack.setNameAndMetadataForDevice(name, rMap.toHashMap());
            promise.resolve(true);
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
