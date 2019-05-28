import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {

    /**
     Initialization methods
    */
    // Method to intialize the SDK with publishable key
    initialize(token) {
        RNHyperTrack.initialize(token);
    },

    // get tracking status
    isTracking() {
        return RNHyperTrack.isTracking();
    },

    getDeviceId() {
        return RNHyperTrack.getDeviceId();
    },
    /**
     Basic integration methods
    */
    // resume tracking
    startTracking() {
        return RNHyperTrack.startTracking();
    },

    // stop tracking
    stopTracking() {
        RNHyperTrack.stopTracking();
    },

    setDevice(name, rMap){
        RNHyperTrack.setDeviceWithName(name,rMap);
    },
    
    sendCustomEvent(rMap){
        RNHyperTrack.sendCustomEventWithMetadata(rMap);
    }
}