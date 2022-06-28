import { View, NativeModules, Button, NativeEventEmitter } from 'react-native';
import React, { useEffect } from 'react';

const { HyperTrackSdk } = NativeModules;

const eventEmitter = new NativeEventEmitter(HyperTrackSdk);
HyperTrackSdk.createCalendarEvent();
const App = () => {
  async function getID() {
    try {
      const r = await HyperTrackSdk.getDeviceID();
      console.log(r);
    } catch (error) {}
  }

  async function init() {
    await HyperTrackSdk.initialize(false, true);
  }
  useEffect(() => {
    const listener = eventEmitter.addListener('Event count', (data) => {
      console.log(data);
    });
    init();
    getID();
    return () => {
      listener.remove();
    };
  }, []);

  const createCalendarEventPromise = async () => {
    try {
      const result = await HyperTrackSdk.createCalendarPromise();
      console.log(result);
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <View style={{ justifyContent: 'center', alignItems: 'center' }}>
      <Button
        title="calendar event promise"
        onPress={createCalendarEventPromise}
      />
    </View>
  );
};

export default App;
