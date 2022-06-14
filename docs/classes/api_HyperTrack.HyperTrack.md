# Class: HyperTrack

[api/HyperTrack](../modules/api_HyperTrack.md).HyperTrack

A root class of the HyperTrack SDK. It is the default export of this library.

**`example`**
```js
import HyperTrack from 'react-native-hypertrack-sdk'
```

## Table of contents

### Constructors

- [constructor](api_HyperTrack.HyperTrack.md#constructor)

### Methods

- [decrease](api_HyperTrack.HyperTrack.md#decrease)
- [getDeviceID](api_HyperTrack.HyperTrack.md#getdeviceid)
- [getLocation](api_HyperTrack.HyperTrack.md#getlocation)
- [increase](api_HyperTrack.HyperTrack.md#increase)
- [isAvailable](api_HyperTrack.HyperTrack.md#isavailable)
- [isLoggingEnabled](api_HyperTrack.HyperTrack.md#isloggingenabled)
- [isRunning](api_HyperTrack.HyperTrack.md#isrunning)
- [isTracking](api_HyperTrack.HyperTrack.md#istracking)
- [onIncrementDecreasedChanged](api_HyperTrack.HyperTrack.md#onincrementdecreasedchanged)
- [onIncrementIncreasedChanged](api_HyperTrack.HyperTrack.md#onincrementincreasedchanged)
- [onTrackingStateChanged](api_HyperTrack.HyperTrack.md#ontrackingstatechanged)
- [setDeviceName](api_HyperTrack.HyperTrack.md#setdevicename)
- [startTracking](api_HyperTrack.HyperTrack.md#starttracking)
- [stopTracking](api_HyperTrack.HyperTrack.md#stoptracking)

## Constructors

### constructor

• **new HyperTrack**()

## Methods

### decrease

▸ `Static` **decrease**(): `Promise`<`number`\>

#### Returns

`Promise`<`number`\>

___

### getDeviceID

▸ `Static` **getDeviceID**(): `Promise`<`string`\>

#### Returns

`Promise`<`string`\>

A string used to identify a device uniquely

___

### getLocation

▸ `Static` **getLocation**(): `Promise`<[`Location`](../modules/types.md#location) \| [`LocationError`](../enums/types.LocationError.md)\>

#### Returns

`Promise`<[`Location`](../modules/types.md#location) \| [`LocationError`](../enums/types.LocationError.md)\>

current latitude and longitude or location error or LocationError

___

### increase

▸ `Static` **increase**(): `Promise`<`number`\>

#### Returns

`Promise`<`number`\>

___

### isAvailable

▸ `Static` **isAvailable**(): `Promise`<`boolean`\>

Device's availability for nearby search

#### Returns

`Promise`<`boolean`\>

true when is available or false when unavailable

___

### isLoggingEnabled

▸ `Static` **isLoggingEnabled**(): `Promise`<`boolean`\>

#### Returns

`Promise`<`boolean`\>

___

### isRunning

▸ `Static` **isRunning**(): `Promise`<`boolean`\>

#### Returns

`Promise`<`boolean`\>

___

### isTracking

▸ `Static` **isTracking**(): `Promise`<`boolean`\>

#### Returns

`Promise`<`boolean`\>

___

### onIncrementDecreasedChanged

▸ `Static` **onIncrementDecreasedChanged**(`listener`): `EmitterSubscription`

#### Parameters

| Name | Type |
| :------ | :------ |
| `listener` | (`trackingState`: `number`) => `void` |

#### Returns

`EmitterSubscription`

___

### onIncrementIncreasedChanged

▸ `Static` **onIncrementIncreasedChanged**(`listener`): `EmitterSubscription`

#### Parameters

| Name | Type |
| :------ | :------ |
| `listener` | (`trackingState`: `number`) => `void` |

#### Returns

`EmitterSubscription`

___

### onTrackingStateChanged

▸ `Static` **onTrackingStateChanged**(`listener`): `EmitterSubscription`

Listen for changes of the Tracking State.

**`example`**
```js
const subscription = HyperTrack.onTrackingStateChanged(trackingState => {
  if (trackingState === 'connected') {
    // ... ready to go
  }
})

// later, to stop listening
subscription.remove()
```

#### Parameters

| Name | Type |
| :------ | :------ |
| `listener` | (`trackingState`: [`TrackingState`](../enums/types.TrackingState.md)) => `void` |

#### Returns

`EmitterSubscription`

___

### setDeviceName

▸ `Static` **setDeviceName**(`deviceName`): `Promise`<`void`\>

#### Parameters

| Name | Type | Description |
| :------ | :------ | :------ |
| `deviceName` | `string` | A device name to be shown in dashboard |

#### Returns

`Promise`<`void`\>

___

### startTracking

▸ `Static` **startTracking**(): `Promise`<`void`\>

Expresses an intent to start location tracking

#### Returns

`Promise`<`void`\>

___

### stopTracking

▸ `Static` **stopTracking**(): `Promise`<`void`\>

Expresses an intent to stop location tracking

#### Returns

`Promise`<`void`\>
