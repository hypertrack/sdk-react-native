// import * as React from 'react';
// import { useState } from 'react';

// import {
//   Alert,
//   KeyboardAvoidingView,
//   Platform,
//   SafeAreaView,
//   ScrollView,
//   StatusBar,
//   StyleSheet,
//   Text,
//   TextInput,
//   TouchableOpacity,
//   View,
// } from 'react-native';
// import HyperTrack from 'react-native-hypertrack-sdk';
// import type {
//   HyperTrackErrors,
//   Location,
//   Metadata,
//   LocationError,
// } from 'src/types';

// const Separator = () => <View style={styles.separator} />;

// const Wrapper = ({
//   title,
//   children,
// }: {
//   title: string;
//   children: React.ReactNode;
// }) => (
//   <View style={styles.wrapper}>
//     <Text style={styles.wrapperText}>{title}</Text>
//     {children}
//   </View>
// );

// const Button = ({
//   title,
//   onPress,
//   disabled,
// }: {
//   title: string;
//   onPress: () => void;
//   disabled?: boolean;
// }) => (
//   <TouchableOpacity onPress={onPress} style={styles.button} disabled={disabled}>
//     <Text style={styles.text}>{title}</Text>
//   </TouchableOpacity>
// );

// export default function App() {
//   const [deviceID, setDeviceID] = useState<string>('second');
//   const [isTracking, setIsTracking] = useState<Boolean>(false);
//   const [location, setLocation] = useState<Location | null>(null);
//   const [availability, setAvailability] = useState<Boolean>(false);
//   const [errors, setErrors] = useState<HyperTrackErrors[]>([]);

//   const [trackingState, setTrackingState] = useState<Boolean>(false);
//   const [locationOnce, setLocationOnce] = useState<Location | null>(null);
//   const [deviceName, setDeviceName] = useState('');
//   const [availabilityOnce, setAvailabilityOnce] = useState<Boolean>(false);
//   const [metadata, setMetadata] = useState<Metadata | null>(null);
//   const [metadataToSet, setMetadataToSet] = useState({});
//   const [metadataInputKey, setMetadataInputKey] = useState('');
//   const [metadataInputValue, setMetadataInputValue] = useState('');
//   const [errorsOnce, setErrorsOnce] = useState<HyperTrackErrors[]>([]);
//   const [geoTag, setGeoTag] = useState<Location | null>(null);

//   const [text, onChangeText] = useState('My phone');

//   const getID = async () => {
//     try {
//       const id = await HyperTrack.getDeviceID();
//       console.log('DeviceID', id);
//       setDeviceID(id);
//     } catch (error) {
//       console.log(error);
//     }
//   };
//   React.useEffect(() => {
//     getID();

//     const locationSubscription = HyperTrack.subscribeToLocation((data) => {
//       setLocation(data);
//     });
//     const availabilitySubscription = HyperTrack.subscribeToAvailability(
//       (data) => {
//         setAvailability(data);
//       }
//     );
//     const trackingSubscription = HyperTrack.subscribeToTracking((status) => {
//       setIsTracking(status);
//     });
//     const errorsSubscriptions = HyperTrack.subscribeToErrors((data) => {
//       setErrors(data);
//     });

//     return () => {
//       HyperTrack.clearSubscriptionToLocation(locationSubscription);
//       HyperTrack.clearSubscriptionToAvailability(availabilitySubscription);
//       HyperTrack.clearSubscriptionToTracking(trackingSubscription);
//       HyperTrack.clearSubscriptionToErrors(errorsSubscriptions);
//     };
//   }, []);

//   const startTracking = async () => {
//     try {
//       const response = await HyperTrack.startTracking();
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const stopTracking = async () => {
//     try {
//       const response = await HyperTrack.stopTracking();
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   function isLocation(loc: Location | LocationError): loc is Location {
//     return (loc as Location).longitude !== undefined;
//   }

//   function isHyperTrackError(
//     error: Location | LocationError
//   ): error is HyperTrackErrors[] {
//     return Array.isArray(error);
//   }

//   const getLocation = async () => {
//     try {
//       const response = await HyperTrack.getLocation();
//       if (isLocation(response)) {
//         setLocationOnce(response);
//       } else if (isHyperTrackError(response)) {
//         Alert.alert('HyperTrack error', JSON.stringify(response));
//       } else {
//         Alert.alert('Location info', response);
//       }
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const getTrackingState = async () => {
//     try {
//       const response = await HyperTrack.isTracking();
//       setTrackingState(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const changeAvailability = async () => {
//     try {
//       const response = await HyperTrack.setAvailability(!availability);
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const getAvailabilityOnce = async () => {
//     try {
//       const response = await HyperTrack.isAvailable();
//       setAvailabilityOnce(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const changeDeviceName = async () => {
//     try {
//       const response = await HyperTrack.setName(text);
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const getDeviceName = async () => {
//     try {
//       const response = await HyperTrack.getName();
//       setDeviceName(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const getMetadata = async () => {
//     try {
//       const response = await HyperTrack.getMetadata();
//       console.log(response);
//       setMetadata(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const addDataToMetadata = () => {
//     if (metadataInputKey === '' || metadataInputValue === '') return;
//     setMetadataToSet({
//       ...metadataToSet,
//       [metadataInputKey]: metadataInputValue,
//     });
//     setMetadataInputKey('');
//     setMetadataInputValue('');
//   };

//   const changeMetadata = async () => {
//     if (Object.keys(metadataToSet).length === 0) return;
//     try {
//       const response = await HyperTrack.setMetadata(metadataToSet);
//       setMetadataToSet({});
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const getErrorsOnce = async () => {
//     try {
//       const response = await HyperTrack.getErrors();
//       setErrorsOnce(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const addGeotag = async () => {
//     try {
//       const response = await HyperTrack.addGeotag({ tag: 1 });
//       if (isLocation(response)) {
//         setGeoTag(response);
//       } else if (isHyperTrackError(response)) {
//         Alert.alert('HyperTrack error', JSON.stringify(response));
//       } else {
//         Alert.alert('Location info', response);
//       }
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   const sync = async () => {
//     try {
//       const response = await HyperTrack.sync();
//       console.log(response);
//     } catch (error) {
//       console.log(error);
//     }
//   };

//   return (
//     <>
//       <StatusBar barStyle={'dark-content'} />
//       <SafeAreaView style={styles.container}>
//         <KeyboardAvoidingView
//           behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
//           style={styles.container}
//         >
//           <View style={styles.paddingHorizontal}>
//             <Wrapper title="Subscriptions:">
//               <View style={styles.rowStyle}>
//                 <Text>Tracking state:</Text>
//                 <Text> {isTracking ? 'is tracking' : 'not tracking'}</Text>
//               </View>
//               <View style={styles.rowStyle}>
//                 <Text>Errors:</Text>
//                 <View style={styles.row}>
//                   {errors.map((error, index) => (
//                     <Text
//                       style={[
//                         index !== 0 || index !== errors.length - 1
//                           ? styles.marginHorizontal
//                           : {},
//                       ]}
//                       key={error}
//                     >
//                       {error}
//                     </Text>
//                   ))}
//                 </View>
//               </View>
//               <View style={styles.rowStyle}>
//                 <Text>Availability:</Text>
//                 <Text>{availability ? 'available' : 'not available'}</Text>
//               </View>
//               <View style={styles.rowStyle}>
//                 <Text>Location:</Text>
//                 {location?.latitude && location.longitude && (
//                   <Text>
//                     latitude: {location?.latitude} longitude:{' '}
//                     {location?.longitude}
//                   </Text>
//                 )}
//               </View>
//             </Wrapper>
//           </View>

//           <ScrollView
//             contentContainerStyle={styles.paddingHorizontal}
//             keyboardShouldPersistTaps="handled"
//           >
//             {deviceID !== '' && (
//               <Text style={styles.textAlignCenter}>
//                 DeviceID: {'\n'} {deviceID}
//               </Text>
//             )}

//             <Separator />
//             <View style={styles.rowSpaceAround}>
//               <Button
//                 title="Start tracking"
//                 onPress={startTracking}
//                 disabled={!!isTracking}
//               />
//               <Button
//                 title="Stop tracking"
//                 onPress={stopTracking}
//                 disabled={!isTracking}
//               />
//             </View>
//             <Separator />

//             <Wrapper title="Get once:">
//               <View style={styles.rowStyle}>
//                 <Button title="isTracking" onPress={getTrackingState} />

//                 <Text>{trackingState ? 'is tracking' : 'not tracking'}</Text>
//               </View>
//               <View style={styles.rowStyle}>
//                 <Button title="location" onPress={getLocation} />

//                 {locationOnce?.latitude && locationOnce.longitude && (
//                   <Text>
//                     latitude: {locationOnce?.latitude} longitude:{' '}
//                     {locationOnce?.longitude}
//                   </Text>
//                 )}
//               </View>

//               <View style={styles.rowStyle}>
//                 <Button title="availability" onPress={getAvailabilityOnce} />
//                 <Text>{availabilityOnce ? 'available' : 'not available'}</Text>
//               </View>

//               <View style={styles.rowStyle}>
//                 <Button title="Device name" onPress={getDeviceName} />
//                 {deviceName !== '' && <Text>{deviceName}</Text>}
//               </View>

//               <View style={styles.rowStyle}>
//                 <Button title="Metadata" onPress={getMetadata} />
//                 {metadata !== null && (
//                   <Text>{JSON.stringify(metadata, null, 2)}</Text>
//                 )}
//               </View>

//               <View style={styles.rowStyle}>
//                 <Button title="Errors" onPress={getErrorsOnce} />

//                 <View style={styles.row}>
//                   {errorsOnce.map((error, index) => (
//                     <Text
//                       style={[
//                         index !== 0 || index !== errors.length - 1
//                           ? styles.marginHorizontal
//                           : {},
//                       ]}
//                       key={`${error} - once - ${index}`}
//                     >
//                       {errorsOnce}
//                     </Text>
//                   ))}
//                 </View>
//               </View>
//             </Wrapper>
//             <Wrapper title="Change device name">
//               <View style={styles.rowStyle}>
//                 <TextInput
//                   style={styles.textInput}
//                   onChangeText={onChangeText}
//                   value={text}
//                 />
//                 <Button title="Apply" onPress={changeDeviceName} />
//               </View>
//             </Wrapper>

//             <Wrapper title="Set metadata">
//               <View style={styles.rowStyle}>
//                 <TextInput
//                   style={styles.textInput}
//                   onChangeText={setMetadataInputKey}
//                   placeholder="key"
//                   value={metadataInputKey}
//                 />
//                 <TextInput
//                   style={styles.textInput}
//                   onChangeText={setMetadataInputValue}
//                   placeholder="value"
//                   value={metadataInputValue}
//                 />
//                 <Button title="Add" onPress={addDataToMetadata} />
//               </View>
//               <Text style={styles.marginBottom}>
//                 {JSON.stringify(metadataToSet, null, 2)}
//               </Text>
//               <View style={styles.rowStyle}>
//                 <Button title="Clear" onPress={() => setMetadataToSet({})} />
//                 <Button title="Set metadata" onPress={changeMetadata} />
//               </View>
//             </Wrapper>
//             <Separator />
//             <Button title="change availability" onPress={changeAvailability} />
//             <Separator />
//             <Button title="add geotag" onPress={addGeotag} />
//             {geoTag !== null && (
//               <Text>
//                 latitude: {geoTag?.latitude} longitude: {geoTag?.longitude}
//               </Text>
//             )}
//             <Separator />
//             <Button title="sync" onPress={sync} />
//           </ScrollView>
//         </KeyboardAvoidingView>
//       </SafeAreaView>
//     </>
//   );
// }

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     justifyContent: 'center',
//     backgroundColor: '#fff'
//   },
//   title: {
//     textAlign: 'center',
//     marginVertical: 8,
//   },
//   fixToText: {
//     flexDirection: 'row',
//     justifyContent: 'space-between',
//   },
//   separator: {
//     marginVertical: 18,
//     borderBottomColor: '#737373',
//     borderBottomWidth: StyleSheet.hairlineWidth,
//   },
//   button: {
//     borderWidth: 1,
//     padding: 10,
//     borderColor: '#06283D',
//     backgroundColor: '#47B5FF',
//     borderRadius: 5,
//   },
//   text: {
//     textAlign: 'center',
//     color: '#fff',
//     textTransform: 'uppercase',
//   },
//   rowStyle: {
//     flexDirection: 'row',
//     justifyContent: 'space-between',
//     marginBottom: 10,
//     alignItems: 'center',
//   },
//   wrapper: {
//     borderColor: '#06283D',
//     borderWidth: 1,
//     borderRadius: 5,
//     paddingHorizontal: 20,
//     marginVertical: 9,
//     backgroundColor: '#DFF6FF',
//   },
//   wrapperText: {
//     fontSize: 18,
//     fontWeight: '600',
//     textAlign: 'center',
//     marginTop: 5,
//     marginBottom: 10,
//   },
//   textInput: {
//     height: 40,
//     marginRight: 12,
//     borderWidth: 1,
//     padding: 10,
//     flex: 1,
//     backgroundColor: '#fff',
//   },
//   paddingHorizontal: { paddingHorizontal: 16 },
//   marginHorizontal: { marginHorizontal: 5 },
//   marginBottom: { marginBottom: 10 },
//   row: { flexDirection: 'row' },
//   textAlignCenter: { textAlign: 'center' },
//   rowSpaceAround: { flexDirection: 'row', justifyContent: 'space-around' },
// });
