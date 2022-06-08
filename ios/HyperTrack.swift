import Foundation
import HyperTrack

@objc(HyperTrackSdk)
class HyperTrackSdk: RCTEventEmitter{

    private var hasListeners = false;
    private var count = 0;

    let hyperTrack = try! HyperTrack(publishableKey: .init(Bundle.main.object(forInfoDictionaryKey: "HyperTrackPublishableKey") as! String)!)

    override init(){
        super.init()

    }

    @objc
    override static func requiresMainQueueSetup() ->Bool{
        return true;
    }

    override func supportedEvents() -> [String]! {
        return ["onIncrement", "onDecrement"]
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

    @objc
    func getDeviceID(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        resolve(hyperTrack.deviceID);
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
        hyperTrack.start()
        resolve("Tracking started")
    }

    @objc
    func stopTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        hyperTrack.stop()
        resolve("Tracking stopped")
    }

    @objc
    func getLocation(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        switch hyperTrack.location {
            case .success(let location):
                resolve(["latitude": location.latitude, "longitude": location.longitude])
            case .failure(let error):
                func returnResult(value: String) {
                  resolve(value)
                }
                switch error {
                case .gpsSignalLost:
                    returnResult(value: "gpsSignalLost")
                case .locationMocked:
                    returnResult(value: "locationMocked")
                case .locationPermissionsCantBeAskedInBackground:
                    returnResult(value: "locationPermissionsCantBeAskedInBackground")
                case .locationPermissionsDenied:
                    returnResult(value: "locationPermissionsDenied")
                case .locationPermissionsInsufficientForBackground:
                    returnResult(value: "locationPermissionsInsufficientForBackground")
                case .locationPermissionsNotDetermined:
                    returnResult(value: "locationPermissionsNotDetermined")
                case .locationPermissionsReducedAccuracy:
                    returnResult(value: "locationPermissionsReducedAccuracy")
                case .locationPermissionsRestricted:
                    returnResult(value: "locationPermissionsRestricted")
                case .locationServicesDisabled:
                    returnResult(value: "locationServicesDisabled")
                case .motionActivityPermissionsCantBeAskedInBackground:
                    returnResult(value: "motionActivityPermissionsCantBeAskedInBackground")
                case .motionActivityPermissionsDenied:
                    returnResult(value: "motionActivityPermissionsDenied")
                case .motionActivityPermissionsNotDetermined:
                    returnResult(value: "motionActivityPermissionsNotDetermined")
                case .motionActivityServicesDisabled:
                    returnResult(value: "motionActivityServicesDisabled")
                case .notRunning:
                    returnResult(value: "notRunning")
                case .starting:
                    returnResult(value: "starting")
            }
        }
    }
    @objc
    func isTracking(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        resolve(hyperTrack.isTracking)
    }

    @objc
    func isRunning(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        resolve(hyperTrack.isRunning)
    }

    @objc
    func availability(_ resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock) -> Void {
        let availability = hyperTrack.availability

        switch availability {
        case .available:
            resolve(true)
        case .unavailable:
            resolve(false)
        default:
            resolve(false)
        }

    }


    @objc
    func setDeviceName(_ deviceName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        hyperTrack.setDeviceName(deviceName)
        resolve("Device name set")
    }


//    @objc
//    func setMetadata(_ deviceName: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
//        hyperTrack.setDeviceMetadata(deviceName)
//        resolve("Device name set")
//    }

}
