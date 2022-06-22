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

    const lc = HyperTrack.onTrackingStateChanged((data) => {
      console.log('onTrackingStateChanged', data);
    });
    const ec = HyperTrack.onErrors((data) => {
      console.log('onErrors', data);
    });
    // const a = HyperTrack.onIncrementIncreasedChanged((data) => {
    //   console.log('onIncrement received', data);
    // });
    // const b = HyperTrack.onIncrementDecreasedChanged((data) => {
    //   console.log('onDecrement received', data);
    // });

    return () => {
      lc.remove();
      ec.remove();
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
      const response = await HyperTrack.setDeviceName('YHiP12');
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const subscribeToLocation = async () => {
    try {
      const response = await HyperTrack.subscribeToLocation();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const subscribeToIsTracking = async () => {
    try {
      const response = await HyperTrack.subscribeToIsTracking();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const subscribeToErrors = async () => {
    try {
      const response = await HyperTrack.subscribeToErrors();
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const cancelErrorSubscription = async () => {
    try {
      const response = await HyperTrack.cancelSubscription(
        'unsubscribeToErrors'
      );
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const cancelTrackingStateSubscription = async () => {
    try {
      const response = await HyperTrack.cancelSubscription(
        'unsubscribeToIsTracking'
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
        <Button title="subscribeToLocation" onPress={subscribeToLocation} />
        <Button title="subscribeToIsTracking" onPress={subscribeToIsTracking} />
        <Button title="subscribeToErrors" onPress={subscribeToErrors} />
        <Button
          title="cancelErrorSubscription"
          onPress={cancelErrorSubscription}
        />
        <Button
          title="cancelTrackingStateSubscription"
          onPress={cancelTrackingStateSubscription}
        />
      </View>
    </>
  );
}
