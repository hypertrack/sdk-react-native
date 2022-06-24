import * as React from 'react';

import { Button, StatusBar, View } from 'react-native';
import HyperTrack from 'react-native-hypertrack-sdk';

export default function App() {
  const getID = async () => {
    try {
      const id = await HyperTrack.getDeviceID();
      console.log('DeviceID', id);
    } catch (error) {
      console.log(error);
    }
  };
  React.useEffect(() => {
    getID();

    const locationSubscription = HyperTrack.subscribeToLocation((data) => {
      console.log('onLocationChanged', data);
    });
    const availabilitySubscription = HyperTrack.subscribeToAvailability((data) => {
      console.log('onAvailableStateChanged', data);
    });
    const trackingSubscription = HyperTrack.subscribeToTracking((data) => {
      console.log('onTrackingStateChanged', data);
    });
    const errorsSubscriptions = HyperTrack.subscribeToErrors((data) => {
      console.log('onErrors', data);
    });

    return () => {
      HyperTrack.clearSubscriptionToLocation(locationSubscription);
      HyperTrack.clearSubscriptionToAvailability(availabilitySubscription);
      HyperTrack.clearSubscriptionToTracking(trackingSubscription);
      HyperTrack.clearSubscriptionToErrors(errorsSubscriptions);
    };
  }, []);

  const startTracking = async () => {
    try {
      const response = await HyperTrack.startTracking();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const stopTracking = async () => {
    try {
      const response = await HyperTrack.stopTracking();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const getLocation = async () => {
    try {
      const response = await HyperTrack.getLocation();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const isTracking = async () => {
    try {
      const response = await HyperTrack.isTracking();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const availability = async () => {
    try {
      const response = await HyperTrack.isAvailable();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const setDeviceName = async () => {
    try {
      const response = await HyperTrack.setName('YHiP12');
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const getMetadata = async () => {
    try {
      const response = await HyperTrack.getMetadata();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const setMetadata = async () => {
    try {
      const response = await HyperTrack.setMetadata({
        vehicle_type: 'scooter',
        group_id: 1,
      });
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const cancelTrackingStateSubscription = async () => {
    try {
      const response = await HyperTrack.cancelSubscription(
        'unsubscribeToTracking'
      );
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <StatusBar barStyle={'dark-content'} />
      <View style={{ justifyContent: 'center', alignItems: 'center', flex: 1 }}>
        <Button title="Start" onPress={startTracking} />
        <Button title="Stop" onPress={stopTracking} />
        <Button title="getlocation" onPress={getLocation} />
        <Button title="isTracking" onPress={isTracking} />
        <Button title="availability" onPress={availability} />
        <Button title="device name" onPress={setDeviceName} />
        <Button title="get metadata" onPress={getMetadata} />
        <Button title="set metadata" onPress={setMetadata} />
        <Button
          title="cancelTrackingStateSubscription"
          onPress={cancelTrackingStateSubscription}
        />
      </View>
    </>
  );
}
