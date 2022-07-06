/*
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useEffect, useRef, useState } from 'react';
import { View, Button, StyleSheet } from 'react-native';

// Import HyperTrack SDK API
// You can also use CriticalErrors to react to different kind of errors preventing tracking (ex: permissions deined)
import HyperTrack from 'react-native-hypertrack-sdk';

const App = () => {
  const hyperTrack = useRef<HyperTrack | null>(null);
  const [enableListeners, setEnableListeners] = useState(false);
  const [availability, setAvailability] = useState(true);
  const init = async () => {
    // (Optional) This turns on logging for underlying native SDKs. Placed on top so SDKs start logging immediately
    // HyperTrack.enableDebugLogging(true);

    try {
      const h = await HyperTrack.initialize(
        '5lKJLpoO8beSzvxQ2XpwiBf_KDI3GHmd03vO4zRejbuwP07sE9201LEpOPDDr_73yenWW9MxcXQQvHnFkZB4kg',
        false
      );
      if (h instanceof HyperTrack) {
        hyperTrack.current = h;
      }
    } catch (e) {
      console.log('EE', e);
    }
    try {
      const ID = await hyperTrack.current?.getDeviceID();
      console.log('ID', ID);

      // (Optional) Set the device name to display in dashboard (for ex. user name)
      hyperTrack.current?.setDeviceName('RN Legacy');

      // (Optional) Attach any JSON metadata to this device to see in HyperTrack's API responses
      hyperTrack.current?.setMetadata({
        driver_id: '111',
        state: 'IN_PROGRESS',
      });
      setEnableListeners(true);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    init();
  }, []);

  useEffect(() => {
    // if (enableListeners) {
    const er = hyperTrack.current?.subscribeToErrors((error) => {
      console.log('Subsc', error);
    });

    const isr = hyperTrack.current?.subscribeToTracking((isTracking) => {
      console.log('isTracking', isTracking);
    });

    return () => {
      er?.remove();
      isr?.remove();
    };
    // }
  }, [enableListeners]);

  const getLocation = async () => {
    const location = await hyperTrack.current?.getLocation();
    console.log('location', location);
  };

  const addGeoTag = async () => {
    const geotag = await hyperTrack.current?.addGeotag({ shop: 'apple' });
    console.log('geotag', geotag);
  };

  const isAvailable = async () => {
    const available = await hyperTrack.current?.isAvailable();
    setAvailability(available ?? false);
    console.log('avail', available);
  };

  const changeAvailability = async () => {
    const a = await hyperTrack.current?.setAvailability(!availability);
    console.log('a', a);
  };

  return (
    <View style={styles.wrapper}>
      <Button
        title="Start"
        onPress={() => hyperTrack.current?.startTracking()}
      />
      <Button title="Stop" onPress={() => hyperTrack.current?.stopTracking()} />
      <Button title="Get location" onPress={getLocation} />
      <Button title="Add geotag" onPress={addGeoTag} />
      <Button title="isAvailable" onPress={isAvailable} />
      <Button title="changeAvailability" onPress={changeAvailability} />
    </View>
  );
};

export default App;

const styles = StyleSheet.create({
  wrapper: { justifyContent: 'center', alignItems: 'center', flex: 1 },
});

// export default class HyperTrackQuickstart extends Component {

//   state = {
//     deviceId: '',
//     trackingState: 'Stopped',
//     isTracking: false,
//   };

//   _initializeHyperTrack = async () => {
//     // (Optional) This turns on logging for underlying native SDKs. Placed on top so SDKs start logging immediately
//     HyperTrack.enableDebugLogging(true);

//     this.hyperTrack = await HyperTrack.createInstance(PUBLISHABLE_KEY, false);

//     // Obtain the unique HyperTrack's DeviceID identifier to use it with HyperTrack's APIs
//     const deviceId = await this.hyperTrack.getDeviceID();
//     console.log(`deviceId = ${deviceId}`);
//     this.setState({ deviceId: deviceId });

//     // (Optional) Set the device name to display in dashboard (for ex. user name)
//     this.hyperTrack.setDeviceName('RN Quickstart');

//     // (Optional) Attach any JSON metadata to this device to see in HyperTrack's API responses
//     this.hyperTrack.setMetadata({
//       driver_id: 'RN Quickstart Driver',
//       state: 'IN_PROGRESS',
//     });

//     // (Optional) Register tracking listeners to update your UI when SDK starts/stops or react to errors
//     this.hyperTrack.registerTrackingListeners(
//       this,
//       // Display or log errors
//       (error) => {
//         if (
//           error.code === CriticalErrors.INVALID_PUBLISHABLE_KEY ||
//           error.code === CriticalErrors.AUTHORIZATION_FAILED
//         ) {
//           console.log(`Initialization failed: ${error.code} ${error.message}`);
//         } else {
//           console.log(`Tracking failed: ${error.code} ${error.message}`);
//         }
//         this.setState({
//           trackingState:
//             'Stopped with error: ' + error.code + ' - ' + error.message,
//           isTracking: false,
//         });
//       },
//       // Update UI when tracking starts
//       () => this.setState({ trackingState: 'Started', isTracking: true }),
//       // Update UI when tracking stops
//       () => this.setState({ trackingState: 'Stopped', isTracking: false })
//     );
//   };

//   // Call the initialization in constructor
//   constructor(props) {
//     super(props);
//     this._initializeHyperTrack();
//   }

//   startTracking = function () {
//     console.log('Start tracking');
//     this.hyperTrack.startTracking();
//   };
//   stopTracking = function () {
//     console.log('Stop tracking');
//     this.hyperTrack.stopTracking();
//   };

//   getotag = async () => {
//     console.log('Add geotag');
//     // this.hyperTrack.addGeotag({'test': true})
//     let expectedLocation = { latitude: 37.7953, longitude: -122.3969 };
//     let result = await this.hyperTrack.addGeotag(
//       { test: true },
//       expectedLocation,
//       true
//     );
//     console.log('Got geotag result ' + result?.code);
//   };

//   // (Optional) Unregister tracking listeners if they were registered in previous step
//   componentWillUnmount() {
//     this.hyperTrack.unregisterTrackingListeners(this);
//   }

//   render() {
//     const { isTracking, trackingState } = this.state;

//     return (
//       <View style={styles.bigContainer}>
//         <Text style={styles.titleText}>{'Device ID (long press to copy)'}</Text>
//         <Text selectable style={styles.text}>
//           {this.state.deviceId}
//         </Text>
//         <Text style={styles.titleText}>{'Tracking state'}</Text>
//         <Text style={styles.text}>{trackingState}</Text>
//         <Button
//           onPress={() => {
//             this.startTracking();
//           }}
//         >
//           Start Tracking
//         </Button>
//         <Button
//           onPress={() => {
//             this.stopTracking();
//           }}
//         >
//           Stop Tracking
//         </Button>
//         <Button
//           onPress={() => {
//             this.getotag();
//           }}
//         >
//           Add Geotag
//         </Button>
//         <View style={styles.buttonContainer}>
//           <Button
//             style={isTracking ? styles.startButton : styles.stopButton}
//             textStyle={styles.buttonText}
//           >
//             {isTracking ? 'Tracking' : 'Not Tracking'}
//           </Button>
//         </View>
//       </View>
//     );
//   }
// }

// const stylesBase = {
//   titleText: {
//     textAlign: 'center',
//     width: '100%',
//     color: '#000000',
//     fontWeight: 'bold',
//     padding: 2,
//     fontSize: 20,
//   },
//   text: {
//     textAlign: 'center',
//     width: '100%',
//     color: '#000000',
//     padding: 10,
//     fontSize: 18,
//   },
//   buttonText: {
//     color: '#ffffff',
//   },
//   bigContainer: {
//     flex: 1,
//     padding: 50,
//     backgroundColor: '#fff',
//     justifyContent: 'flex-start',
//   },
//   buttonContainer: {
//     flex: 1,
//     backgroundColor: '#fff',
//     justifyContent: 'flex-end',
//   },
//   middleButtonContainer: {
//     flex: 1,
//     backgroundColor: '#fff',
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   startButton: {
//     backgroundColor: '#00ce5b',
//     borderWidth: 0,
//   },
//   stopButton: {
//     backgroundColor: '#040504',
//     borderWidth: 0,
//   },
// };

// const styles = StyleSheet.create(stylesBase);
