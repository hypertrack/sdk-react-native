import Foundation
import HyperTrack

@objc(HyperTrackSdk)
class HyperTrackSdk: RCTEventEmitter{

    private var hasListeners = false;
    private var count = 0;

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
}
