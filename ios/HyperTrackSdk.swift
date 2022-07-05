// import HyperTrack

// @objc(HyperTrackSdk)
// class HyperTrackSdk: RCTEventEmitter{

//     private var hasListeners = false
//     private var listeners: [String: HyperTrack.Cancel] = [:]

//     //    let hyperTrack = try! HyperTrack(publishableKey: .init(Bundle.main.object(forInfoDictionaryKey: "HyperTrackPublishableKey") as! String)!)

//     override init(){
//         super.init()
//     }

//     @objc
//     override static func requiresMainQueueSetup() ->Bool{
//         return true;
//     }

//     override func supportedEvents() -> [String]! {
//         return ["onIncrement", "onDecrement", "onLocationChanged", "onTrackingStateChanged", "onErrors", "onAvailableStateChanged"]
//     }

//     @objc
//     override func constantsToExport() -> [AnyHashable : Any]! {
//         return ["initialCount": 0]
//     }

//     override func startObserving() {
//         hasListeners = true;
//         // setup listeners
//     }

//     override func stopObserving() {
//         hasListeners = false;
//         // remove listeners
//     }

//     func addListener(_ name: String, listener: @escaping HyperTrack.Cancel) {
//         listeners[name] = listener
//     }

//     @objc
//     func getDeviceID(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.deviceID);
//     }

//     @objc
//     func getName(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.name)
//     }

//     @objc
//     func setName(_ name: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
//         HyperTrack.name = name
//         resolve("Device name has been set")
//     }

//     @objc
//     func getMetadata(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.metadata.json.toBridge())
//     }

//     @objc
//     func setMetadata(_ metadata: [String: Any], resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
//         metadata.forEach {
//             let value = HyperTrack.JSON.fromBridge($0.value)
//             HyperTrack.metadata[$0.key] = value
//         }
//         resolve("Successfuly set")
//     }

//     @objc
//     func getLocation(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         switch HyperTrack.location {
//         case .success(let location):
//             resolve(location.json.toBridge())
//         case .failure(let error):
//             resolve(error.json.toBridge())
//         }
//     }

//     @objc
//     func isAvailable(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.isAvailable)
//     }

//     @objc
//     func setAvailability(_ availability: Bool, resolve:RCTPromiseResolveBlock, rejecter reject:RCTPromiseRejectBlock) -> Void {
//         HyperTrack.isAvailable = availability
//         resolve("Availability has been set to \(availability)")
//     }

//     @objc
//     func isTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.isTracking)
//     }

//     @objc
//     func getErrors(_ resolve:RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         resolve(HyperTrack.errors.map { $0.json.toBridge() })
//     }

//     @objc
//     func startTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         HyperTrack.startTracking()
//         resolve("Tracking started")
//     }

//     @objc
//     func stopTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         HyperTrack.stopTracking()
//         resolve("Tracking stopped")
//     }

//     @objc
//     func addGeotag(_ metadata: [String: Any], resolve:RCTPromiseResolveBlock, rejecter reject:RCTPromiseRejectBlock) -> Void {
//         var dictionary:[String:HyperTrack.JSON] = [:]
//         metadata.forEach {
//             let value = HyperTrack.JSON.fromBridge($0.value)
//             dictionary[$0.key] = value
//         }
//         let result = HyperTrack.addGeotag(dictionary)
//         switch result {
//         case .success(let location):
//             resolve(location.json.toBridge())
//         case .failure(let error):
//             resolve(error.json.toBridge())
//         }
//     }

//     @objc
//     func sync(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
//         HyperTrack.sync()
//         resolve("Synced")
//     }

//     @objc
//     func subscribeToLocation(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         let cancel = HyperTrack.subscribeToLocation(){
//             result in

//             switch result {
//             case .success(let location):
//                 self.sendEvent(withName: "onLocationChanged", body: location.json.toBridge())
//             case .failure(let error):
//                 print("subscribeToLocation error \(error)")
//             }

//         }
//         addListener("unsubscribeToLocation",  listener: cancel)
//         resolve("subscribed")
//     }

//     @objc
//     func subscribeToAvailability(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         let cancel = HyperTrack.subscribeToIsAvailable() { isAvailable in
//             self.sendEvent(withName: "onAvailableStateChanged", body: isAvailable)
//         }
//         addListener("unsubscribeToAvailability", listener: cancel)
//         resolve("subscribed")
//     }

//     @objc
//     func subscribeToTracking(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         let handler: (Bool) -> Void = { (result: Bool) in
//             self.sendEvent(withName: "onTrackingStateChanged", body: result)
//         }
//         let cancel = HyperTrack.subscribeToIsTracking(completionHandler: handler)
//         addListener("unsubscribeToTracking", listener: cancel)
//         resolve("subscribed")

//     }

//     @objc
//     func subscribeToErrors(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         let cancel = HyperTrack.subscribeToErrors() { errorsSet in
//             self.sendEvent(withName: "onErrors", body: errorsSet.map { $0.json.toBridge() })
//         }
//         addListener("unsubscribeToErrors", listener: cancel)
//         resolve("subscribed")
//     }

//     @objc
//     func cancelSubscription(_ subscriptionName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
//         if let listener = listeners.removeValue(forKey: subscriptionName) {
//             listener()
//             resolve("unsubscribed")
//         }
//     }

//     @objc
//     func cancelAllSubscriptions(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
//         listeners.forEach {
//             $0.value()
//         }
//         listeners.removeAll()
//         resolve("unsubscribed")
//     }
// }

// // MARK: - Bridge helpers

// // source: https://github.com/launchdarkly/react-native-client-sdk/blob/517f8205ba9a610da08fff9e75cd4321bd42623e/ios/LaunchdarklyReactNativeClient.swift#L324-L356
// extension HyperTrack.JSON {
//     static func fromBridge(_ value: Any) -> HyperTrack.JSON {
//         guard !(value is NSNull) else { return .null }
//         if let nsNumValue = value as? NSNumber {
//             // Because we accept `LDValue` in contexts that can receive anything, the value is a
//             // reference type in Objective-C. Because of that, RN bridges the type as a `NSNumber`,
//             // so we must determine whether that `NSNumber` was originally created from a `BOOL`.
//             // Adapted from https://stackoverflow.com/a/30223989
//             let boolTypeId = CFBooleanGetTypeID()
//             if CFGetTypeID(nsNumValue) == boolTypeId {
//                 return .bool(nsNumValue.boolValue)
//             } else {
//                 return .number(Double(truncating: nsNumValue))
//             }
//         }
//         if let stringValue = value as? String { return .string(stringValue) }
//         if let arrayValue = value as? [Any] { return .array(arrayValue.map { fromBridge($0) }) }
//         if let dictValue = value as? [String: Any] { return .object(dictValue.mapValues { fromBridge($0) }) }
//         return .null
//     }

//     func toBridge() -> Any {
//         switch self {
//         case .null: return NSNull()
//         case .bool(let boolValue): return boolValue
//         case .number(let numValue): return numValue
//         case .string(let stringValue): return stringValue
//         case .array(let arrayValue): return arrayValue.map { $0.toBridge() }
//         case .object(let objectValue): return objectValue.mapValues { $0.toBridge() }
//         @unknown default: fatalError("unhandled case")
//         }
//     }
// }

// // MARK: Bridged Data Types

// extension HyperTrack.Location {
//     var json: HyperTrack.JSON {
//         .object(["latitude": .number(latitude), "longitude": .number(longitude)])
//     }
// }

// extension HyperTrack.LocationError {
//     var json: HyperTrack.JSON {
//         switch self {
//         case .notRunning, .starting:
//             return .string(String(describing: self))
//         case .errors(let errors):
//             return .array(errors.map(\.json))
//         @unknown default:
//             fatalError("unhandled location error")
//         }
//     }
// }

// extension HyperTrack.HyperTrackError {
//     var json: HyperTrack.JSON {
//         .string(string)
//     }
// }

// extension HyperTrack.JSON.Object {
//     var json: HyperTrack.JSON { .object(self) }
// }
