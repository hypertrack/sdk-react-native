import Foundation
import HyperTrack

@objc(HyperTrackSdk)
class HyperTrackSdk: RCTEventEmitter{

    private var hasListeners = false
    private var count = 0
    private var listeners: [String: HyperTrack.Cancel] = [:]

//    let hyperTrack = try! HyperTrack(publishableKey: .init(Bundle.main.object(forInfoDictionaryKey: "HyperTrackPublishableKey") as! String)!)

    override init(){
        super.init()
    }

    @objc
    override static func requiresMainQueueSetup() ->Bool{
        return true;
    }

    override func supportedEvents() -> [String]! {
        return ["onIncrement", "onDecrement", "onLocationChanged", "onTrackingStateChanged", "onErrors"]
    }

    @objc
    override func constantsToExport() -> [AnyHashable : Any]! {
        return ["initialCount": 0]
    }

    override func startObserving() {
        hasListeners = true;

        // setup listeners
    }

    override func stopObserving() {
        hasListeners = false;

        // remove listeners
    }

    func addListener(_ name: String, listener: @escaping HyperTrack.Cancel) {
        if listeners.contains (where: { $0.key == name }) { return }
        listeners[name] = listener
    }

    @objc
    func getDeviceID(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        resolve(HyperTrack.deviceID);
    }

    @objc
    func increment(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        count += 1;
        resolve(count)

        sendEvent(withName: "onIncrement", body: ["count increased", count])
    }

    @objc
    func decrement(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        if(count == 0) {
            let error = NSError(domain: "", code: 200, userInfo: nil )
            reject("ERROR_COUNT", "count cannot be negative", error)
        } else {
            count -= 1;
            resolve("count is \(count)")

            sendEvent(withName: "onDecrement", body: ["count decreased", count])
        }
    }

    @objc
    func startTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        HyperTrack.startTracking()
        resolve("Tracking started")
    }

    @objc
    func stopTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        HyperTrack.stopTracking()
        resolve("Tracking stopped")
    }

    @objc
    func getLocation(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        switch HyperTrack.location {
            case .success(let location):
              resolve(location.json.toBridge())

            case .failure(let error):
            func returnResult(value: String) {
              resolve(value)
            }
            switch error {
                case .notRunning:
                    returnResult(value: "notRunning")
                case .starting:
                    returnResult(value: "starting")
                case .errors(let errorsSet):
                    resolve(errorsSet.map { $0.string })
                default:
                    resolve("something went wrong")
            }
        }
    }
    @objc
    func isTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        resolve(HyperTrack.isTracking)
    }

    @objc
    func availability(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        resolve(HyperTrack.isAvailable)
    }

    @objc
    func setDeviceName(_ deviceName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        HyperTrack.name = deviceName
        resolve("Device name set")
    }

    @objc
    func subscribeToLocation(_ deviceName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        let cancel = HyperTrack.subscribeToLocation(){
            result in

            switch result {
                case .success(let location):
                self.sendEvent(withName: "onLocationChanged", body: ["latitude": location.latitude, "longitude": location.longitude])
                case .failure(let error):
                print("subscribeToLocation error \(error)")
            }

        }
        addListener("unsubscribeToLocation",  listener: cancel)
        resolve("subscribed")
    }

    @objc
    func subscribeToIsTracking(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        let handler: (Bool) -> Void = { (result: Bool) in
            self.sendEvent(withName: "onTrackingStateChanged", body: result)
        }
        let cancel = HyperTrack.subscribeToIsTracking(completionHandler: handler)
        addListener("unsubscribeToIsTracking", listener: cancel)
        resolve("subscribed")

    }

    @objc
    func subscribeToErrors(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        let cancel = HyperTrack.subscribeToErrors() { errorsSet in
            self.sendEvent(withName: "onErrors", body: errorsSet.map { $0.string })
        }
        addListener("unsubscribeToErrors", listener: cancel)
        resolve("subscribed")
    }

    @objc
    func cancelSubscription(_ subscriptionName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        if listeners.contains (where: { $0.key == subscriptionName }) {
            let cancel = listeners[subscriptionName]
            if (cancel != nil) {
                listeners.removeValue(forKey: subscriptionName)
                cancel!()
                resolve("unsubscribed")
            }
        }
    }
}

// MARK: - Bridge helpers

// source: https://github.com/launchdarkly/react-native-client-sdk/blob/517f8205ba9a610da08fff9e75cd4321bd42623e/ios/LaunchdarklyReactNativeClient.swift#L324-L356
extension HyperTrack.JSON {
  static func fromBridge(_ value: Any) -> HyperTrack.JSON {
    guard !(value is NSNull) else { return .null }
    if let nsNumValue = value as? NSNumber {
        // Because we accept `LDValue` in contexts that can receive anything, the value is a
        // reference type in Objective-C. Because of that, RN bridges the type as a `NSNumber`,
        // so we must determine whether that `NSNumber` was originally created from a `BOOL`.
        // Adapted from https://stackoverflow.com/a/30223989
        let boolTypeId = CFBooleanGetTypeID()
        if CFGetTypeID(nsNumValue) == boolTypeId {
            return .bool(nsNumValue.boolValue)
        } else {
            return .number(Double(truncating: nsNumValue))
        }
    }
    if let stringValue = value as? String { return .string(stringValue) }
    if let arrayValue = value as? [Any] { return .array(arrayValue.map { fromBridge($0) }) }
    if let dictValue = value as? [String: Any] { return .object(dictValue.mapValues { fromBridge($0) }) }
    return .null
  }

  func toBridge() -> Any {
    switch self {
    case .null: return NSNull()
    case .bool(let boolValue): return boolValue
    case .number(let numValue): return numValue
    case .string(let stringValue): return stringValue
    case .array(let arrayValue): return arrayValue.map { $0.toBridge() }
    case .object(let objectValue): return objectValue.mapValues { $0.toBridge() }
    @unknown default: fatalError("unhandled case")
    }
  }
}

// MARK: Bridged Data Types

extension HyperTrack.Location {
  var json: HyperTrack.JSON {
    .object(["latitude": .number(latitude), "longitude": .number(longitude)])
  }
}

extension HyperTrack.LocationError {
  var json: HyperTrack.JSON {
    switch self {
    case .notRunning, .starting:
      return .string(String(describing: self))
    case .errors(let errors):
      return .array(errors.map(\.json))
    @unknown default:
      fatalError("unhandled location error")
    }
  }
}

extension HyperTrack.HyperTrackError {
  var json: HyperTrack.JSON {
    .string("HyperTrackError: \(string)")
  }
}
