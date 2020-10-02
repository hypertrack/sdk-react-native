'use strict';

import EventEmitter from './EventEmitter';
import {Error} from './CriticalErrors';

const HyperTrack = require('react-native').NativeModules.HyperTrack;

class HyperTrackAPI {

    /**
     * Returns a string which is used by HyperTrack to uniquely identify the user.
     */
    getDeviceID(): Promise<string>  {
        return HyperTrack.getDeviceID();
    }

    /**
     * Determine whether the SDK is tracking the movement of the user.
     * @return {boolean} Whether the user's movement data is getting tracked or not.
     */
    isTracking(): Promise<boolean> {
        return HyperTrack.isTracking();
    }

    /**
     * Update device state from server.
     */
    syncDeviceSettings() {
        return HyperTrack.syncDeviceSettings();
    }

    /**
     * Start or resume tracking location and activity events.
     */
    startTracking() {
        HyperTrack.startTracking();
    }

    /**
     * Stops the SDK from listening to the user's movement updates and recording any data.
     */
    stopTracking() {
        HyperTrack.stopTracking();
    }

    /**
     * Registers tracking state callbacks.
     * @param {Object} component - Instance of your component.
     * @param {function} onError - Tracking state callback for error events.
     * @param {function} onStart - Tracking state callback for start event.
     * @param {function} onStop - Tracking state callback for stop event.
     */
    registerTrackingListeners(component: Object,
                         onError: (error: ?Error) => void,
                         onStart: () => void,
                         onStop: () => void) {
        if (onError) component.onErrorHyperTrackSubscription = EventEmitter.addListener('onTrackingErrorHyperTrack', onError);
        if (onStart) component.onStartHyperTrackSubscription = EventEmitter.addListener('onTrackingStartHyperTrack', onStart);
        if (onStop) component.onStopHyperTrackSubscription = EventEmitter.addListener('onTrackingStopHyperTrack', onStop);
        HyperTrack.subscribeOnEvents();
    }

    /**
     * Removes tracking state callbacks.
     * @param {Object} component - Instance of your component.
     */
    unregisterTrackingListeners(component: Object) {
        component.onErrorHyperTrackSubscription && component.onErrorHyperTrackSubscription.remove();
        component.onStartHyperTrackSubscription && component.onStartHyperTrackSubscription.remove();
        component.onStopHyperTrackSubscription && component.onStopHyperTrackSubscription.remove();
    }

    /**
     * Send device name to HyperTrack
     * @param {string} name - Device name you want to see in the Dashboard.
     */
    setDeviceName(name: string) {
        HyperTrack.setDeviceName(name);
    }

    /**
     * Send metadata details to HyperTrack
     * @param {Object} data - Send extra device information.
     */
    setMetadata(data: Object) {
        HyperTrack.setMetadata(data);
    }

    /**
     * Set a trip marker
     * @param {Object} data - Include anything that can be parsed into JSON.
     */
    setTripMarker(data: Object) {
        HyperTrack.setTripMarker(data);
    }

}

module.exports = {
    /**
     * Initialize the HyperTrack SDK
     * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
     * @param {boolean} automaticallyRequestPermissions - If true, SDK automatically triggers location and motion activity permissions dialogs when tracking starts.
     */
    async createInstance(publishableKey, automaticallyRequestPermissions = true): Promise<HyperTrackAPI> {
        await HyperTrack.initialize(publishableKey, false, automaticallyRequestPermissions);
        return new HyperTrackAPI();
    },
    /**
     * Enables debug log output. This is very verbose, so you shouldn't use this for
     * production build.
     */
    enableDebugLogging(enable) {
        HyperTrack.enableDebugLogging(enable);
    },
};
