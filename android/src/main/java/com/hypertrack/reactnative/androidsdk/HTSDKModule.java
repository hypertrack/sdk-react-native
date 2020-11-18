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
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;
import com.hypertrack.sdk.persistence.DataStore;
import com.hypertrack.sdk.utils.StaticUtilsAdapter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import com.hypertrack.sdk.views.HyperTrackViews;
import androidx.core.util.Consumer;
import com.google.gson.Gson;
import com.hypertrack.sdk.views.DeviceUpdatesHandler;
import com.hypertrack.sdk.views.dao.Location;
import com.hypertrack.sdk.views.dao.MovementStatus;
import com.hypertrack.sdk.views.dao.StatusUpdate;
import com.hypertrack.sdk.views.dao.Trip;
import androidx.annotation.NonNull;
import java.util.Locale;
@ReactModule(name = HTSDKModule.NAME)
public class HTSDKModule extends ReactContextBaseJavaModule {
    private static final String TAG = "HTSDKModule";
    public static final String NAME = "HyperTrack";
    public TrackingStateObserver.OnTrackingStateChangeListener trackingStateChangeListener;
    public HyperTrack sdkInstance;
    public DeviceUpdatesHandler deviceUpdatesHandler;
    public HyperTrackViews mHyperTrackView;
    final Gson gson = new Gson();
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
            sdkInstance = HyperTrack.getInstance(
                    publishableKey
            );
           mHyperTrackView= HyperTrackViews.getInstance(getReactApplicationContext().getApplicationContext() , publishableKey);
            promise.resolve(true);
        } else {
            Log.d(TAG, "Hypertrack SDK initialization failed while: ApplicationContext is null");
            promise.reject(TAG + "_ERROR", new Exception("Hypertrack SDK initialization failed while: ApplicationContext is null"));
        }
    }
    @ReactMethod
    public void subscribeOnEvents() {
        if (trackingStateChangeListener != null) {
            sdkInstance.removeTrackingStateListener(trackingStateChangeListener);
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
        sdkInstance.addTrackingStateListener(trackingStateChangeListener);
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
    public void onTrackingStart() {
        sdkInstance.start();
    }
    @ReactMethod
    public void onTrackingStop() {
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
            sdkInstance.enableDebugLogging();
        }
    }
    @ReactMethod
    public void enableMockLocation(Boolean enable) {
        if (enable) {
             sdkInstance.enableMockLocation();
        }
    }
    @ReactMethod
    public void getDeviceMovementStatus(final Promise promise){
        Log.d("getDeviceId--- ",""+sdkInstance.getDeviceId());
    mHyperTrackView.getDeviceMovementStatus(sdkInstance.getDeviceId(),
                new Consumer<MovementStatus>() {
                    @Override
                    public void accept(MovementStatus movementStatus) { 
                    Log.d("movementStatus",""+gson.toJson(movementStatus));
                    promise.resolve(gson.toJson(movementStatus));
                    }
                });
    }
    @ReactMethod
    public void subscribeToDeviceUpdates(final Promise promise){
        if (deviceUpdatesHandler != null) {
             mHyperTrackView.stopAllUpdates();
            deviceUpdatesHandler = null;
        }
    mHyperTrackView.subscribeToDeviceUpdates(sdkInstance.getDeviceId(),
                deviceUpdatesHandler=new DeviceUpdatesHandler() {
                    @Override
                    public void onLocationUpdateReceived(@NonNull Location location) {
                        Log.d(TAG, "onLocationUpdateReceived: " + gson.toJson(location));
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onLocationUpdateReceived", gson.toJson(location));
                    }
                    @Override
                    public void onBatteryStateUpdateReceived(@MovementStatus.BatteryState int i) {
                        Log.d(TAG, "onBatteryStateUpdateReceived: " + i);
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onBatteryStateUpdateReceived", i);
                    }
                    @Override
                    public void onStatusUpdateReceived(@NonNull StatusUpdate statusUpdate) {
                        Log.d(TAG, "onStatusUpdateReceived: " + gson.toJson(statusUpdate));
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onStatusUpdateReceived", gson.toJson(statusUpdate));
                    }
                    @Override
                    public void onTripUpdateReceived(@NonNull Trip trip) {
                        Log.d(TAG, "onTripUpdateReceived: " + gson.toJson(trip));
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onTripUpdateReceived", gson.toJson(trip));
                    }
                    @Override
                    public void onError(Exception exception, String deviceId) {
                        Log.w(TAG, "onError: ", exception);
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onErrorDeviceUpdates", exception);
                    }
                    @Override
                    public void onCompleted(String deviceId) {
                        Log.d(TAG, "onCompleted: " + deviceId);
                         getReactApplicationContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("onCompletedDeviceUpdates", deviceId);
                    }
                });
}
    private static final int RN_ERROR_GENERAL = 0;
    private static final int RN_ERROR_INVALID_PUBLISHABLE_KEY = 1;
    private static final int RN_ERROR_AUTHORIZATION_FAILED = 2;
    private static final int RN_ERROR_PERMISSION_DENIED = 3;
    private int convertToRNErrorsCode(int androidSdkCode) {
         Log.d(TAG, "RN_ERRORandroidSdkCode: " + androidSdkCode);
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
