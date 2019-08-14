'use strict';

import EventEmitter from './EventEmitter';
import {Error} from './CriticalErrors';

const HyperTrack = require('react-native').NativeModules.HyperTrack;

module.exports = {
    /**
     * Initialize the HyperTrack SDK
     * @param {string} publishableKey - A unique String that is used to identify your account with HyperTrack.
     * @param {bool} startsTracking - Should the SDK start tracking immediately after initialization
     */
    initialize(publishableKey, startsTracking = true) {
        HyperTrack.initialize(publishableKey, startsTracking);
    },
    /**
     * Determine whether the SDK is tracking the movement of the user.
     * @return {boolean} Whether the user's movement data is getting tracked or not.
     */
    isTracking(): Promise<boolean> {
        return HyperTrack.isTracking();
    },
    /**
     * Returns a string which is used by HyperTrack to uniquely identify the user.
     */
    getDeviceID(): Promise<string>  {
        return HyperTrack.getDeviceID();
    },
    /**
     * Start or resume tracking location and activity events.
     */
    startTracking() {
        return HyperTrack.startTracking();
    },
    /**
     * Stops the SDK from listening to the user's movement updates and recording any data.
     */
    stopTracking() {
        HyperTrack.stopTracking();
    },
    /**
     * Registers tracking state callbacks.
     * @param {Object} component - Instance of your component.
     * @param {function} onError - Tracking state callback for error events.
     * @param {function} onStart - Tracking state callback for start event.
     * @param {function} onStop - Tracking state callback for stop event.
     */
    addTrackingListeners(component: Object,
                         onError: (error: ?Error) => void,
                         onStart: () => void,
                         onStop: () => void) {
        if (onError) component.onErrorHyperTrackSubscription = EventEmitter.addListener('onTrackingErrorHyperTrack', onError);
        if (onStart) component.onStartHyperTrackSubscription = EventEmitter.addListener('onTrackingStartHyperTrack', onStart);
        if (onStop) component.onStopHyperTrackSubscription = EventEmitter.addListener('onTrackingStopHyperTrack', onStop);
        HyperTrack.subscribeOnEvents();
    },
    /**
     * Removes tracking state callbacks.
     * @param {Object} component - Instance of your component.
     */
    removeTrackingListeners(component: Object) {
        component.onErrorHyperTrackSubscription && component.onErrorHyperTrackSubscription.remove();
        component.onStartHyperTrackSubscription && component.onStartHyperTrackSubscription.remove();
        component.onStopHyperTrackSubscription && component.onStopHyperTrackSubscription.remove();
    },
    /**
     * Send device name and metadata details to HyperTrack
     * @param {string} name - Device name you want to see in the Dashboard.
     * @param {Object} data - Send extra device information.
     */
    setDevice(name: string, data: ?Object): Promise<?string>  {
        return HyperTrack.setDevice(name, data);
    },
    /**
     * Set a trip marker
     * @param {Object} data - Include anything that can be parsed into JSON.
     */
    setTripMarker(data: Object): Promise<?string>  {
        return HyperTrack.setTripMarker(data);
    },
    /**
     * Enables debug log output. This is very verbose, so you shouldn't use this for
     * production build.
     */
    enableDebugLogging(enable) {
        HyperTrack.enableDebugLogging(enable);
    },
};
