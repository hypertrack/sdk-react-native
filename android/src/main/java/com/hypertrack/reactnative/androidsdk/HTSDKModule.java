package com.hypertrack.reactnative.androidsdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingInitDelegate;
import com.hypertrack.sdk.TrackingInitError;
import com.hypertrack.sdk.logger.HTLogger;


@ReactModule(name = HTSDKModule.NAME)
public class HTSDKModule extends ReactContextBaseJavaModule implements PermissionListener {

    private static final String TAG = HTSDKModule.class.getSimpleName();

    public static final String NAME = "HyperTrack";

    public HTSDKModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void initialize(String publishableKey, final Boolean startsTracking) {
        if (getCurrentActivity() != null) {
            HyperTrack.initialize(getCurrentActivity(),
                    publishableKey, startsTracking, startsTracking, new TrackingInitDelegate() {
                        @Override
                        public void onError(TrackingInitError trackingInitError) {
                            WritableMap params = Arguments.createMap();
                            params.putInt("code", convertToRNErrorsCode(trackingInitError));
                            getReactApplicationContext()
                                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                    .emit("onTrackingErrorHyperTrack", params);
                        }

                        @Override
                        public void onSuccess() {
                            if (startsTracking) {
                                getReactApplicationContext()
                                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                        .emit("onTrackingStartHyperTrack", null);
                            }
                        }
                    });
        } else {
            Log.d(TAG, "Hypertrack SDK initialization failed while: CurrentActivity is null");
        }
    }

    @ReactMethod
    public void subscribeOnEvents() {
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
        HyperTrack.startTracking(true, new TrackingInitDelegate() {
            @Override
            public void onError(TrackingInitError trackingInitError) {
                WritableMap params = Arguments.createMap();
                params.putInt("code", convertToRNErrorsCode(trackingInitError));
                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTrackingErrorHyperTrack", params);
            }

            @Override
            public void onSuccess() {
                getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTrackingStartHyperTrack", null);
            }
        });
    }

    @ReactMethod
    public void stopTracking() {
        HyperTrack.stopTracking();
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onTrackingStopHyperTrack", null);
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

    private int convertToRNErrorsCode(TrackingInitError trackingInitError) {
        if (trackingInitError instanceof TrackingInitError.InvalidPublishableKeyError) {
            return RN_ERROR_INVALID_PUBLISHABLE_KEY;
        } else if (trackingInitError instanceof TrackingInitError.AuthorizationError) {
            return RN_ERROR_AUTHORIZATION_FAILED;
        } else if (trackingInitError instanceof TrackingInitError.PermissionDeniedError) {
            return RN_ERROR_PERMISSION_DENIED;
        } else {
            return RN_ERROR_GENERAL;
        }
    }

    private static final int REQUEST_LOCATION_PERMISSIONS = 21012;

    private boolean checkLocationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED == getReactApplicationContext().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)
                || getCurrentActivity() == null) {
            return true;

        }
        PermissionAwareActivity activity = getPermissionAwareActivity();
        activity.requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_LOCATION_PERMISSIONS, this);
        return false;
    }


    private PermissionAwareActivity getPermissionAwareActivity() {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            throw new IllegalStateException(
                    "Tried to use permissions API while not attached to an " + "Activity.");
        } else if (!(activity instanceof PermissionAwareActivity)) {
            throw new IllegalStateException(
                    "Tried to use permissions API but the host Activity doesn't"
                            + " implement PermissionAwareActivity.");
        }
        return (PermissionAwareActivity) activity;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        HTLogger.d(TAG, "Location permission was granted");
                        startTracking();
                        return true;

                    } else {
                        HTLogger.d(TAG, "Location permission was denied");
                    }
                }
            }
        }
        return false;
    }
}
