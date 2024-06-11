import HyperTrack

struct Subscriptions {
    let locationSubscription: HyperTrack.Cancellable
    let isAvailableSubscription: HyperTrack.Cancellable
    let isTrackingSubscription: HyperTrack.Cancellable
    let errorsSubscription: HyperTrack.Cancellable
}

@objc(HyperTrackReactNativePlugin)
class HyperTrackReactNativePlugin: RCTEventEmitter {
    private let eventErrors = "errors"
    private let eventIsAvailable = "isAvailable"
    private let eventIsTracking = "isTracking"
    private let eventLocate = "locate"
    private let eventLocation = "location"

    private var subscriptions: Subscriptions? = nil
    private var locateSubscription: HyperTrack.Cancellable? = nil

    @objc override static func requiresMainQueueSetup() -> Bool {
        return false
    }

    @objc override func supportedEvents() -> [String] {
        return [
            eventErrors,
            eventIsAvailable,
            eventIsTracking,
            eventLocate,
            eventLocation,
        ]
    }

    @objc override func addListener(_ eventName: String) {
        // Called when any listener is added.
        super.addListener(eventName)
        /*
         Due to the issue in RN (probably related to the fact that the SDK calls the subscription
         callback too fast) we can't init listener in init() anymore.
         So we init them lazily here.
         */
        if subscriptions == nil {
            subscriptions = initListeners()
        }
        switch eventName {
        case eventIsTracking:
            sendEvent(withName: eventIsTracking, body: serializeIsTracking(HyperTrack.isTracking))
        case eventIsAvailable:
            sendEvent(withName: eventIsAvailable, body: serializeIsAvailable(HyperTrack.isAvailable))
        case eventErrors:
            sendEvent(withName: eventErrors, body: serializeErrors(HyperTrack.errors))
        case eventLocation:
            sendEvent(withName: eventLocation, body: serializeLocationResult(HyperTrack.location))
        default:
            return
        }
    }

    @objc func addGeotag(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.addGeotag(
                args as! [String: Any]
            ),
            method: .addGeotag,
            resolve,
            reject
        )
    }

    @objc func getDeviceId(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getDeviceID(),
            method: .getDeviceID,
            resolve,
            reject
        )
    }

    @objc func getDynamicPublishableKey(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getDynamicPublishableKey(),
            method: .getDynamicPublishableKey,
            resolve,
            reject
        )
    }

    @objc func getErrors(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getErrors(),
            method: .getErrors,
            resolve,
            reject
        )
    }

    @objc func getIsAvailable(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getIsAvailable(),
            method: .getIsAvailable,
            resolve,
            reject
        )
    }

    @objc func getIsTracking(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getIsTracking(),
            method: .getIsTracking,
            resolve,
            reject
        )
    }

    @objc func getLocation(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getLocation(),
            method: .getLocation,
            resolve,
            reject
        )
    }

    @objc func getMetadata(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getMetadata(),
            method: .getMetadata,
            resolve,
            reject
        )
    }

    @objc func getName(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.getName(),
            method: .getName,
            resolve,
            reject
        )
    }

    @objc func locate(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if let locateSubscription = locateSubscription {
            locateSubscription.cancel()
        }
        locateSubscription = HyperTrack.locate { locateResult in
            self.sendEvent(withName: self.eventLocate, body: serializeLocateResult(locateResult))
        }
        sendAsPromise(
            .success(.void),
            method: .locate,
            resolve,
            reject
        )
    }

    @objc func setDynamicPublishableKey(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setDynamicPublishableKey(
                args as! [String: Any]
            ),
            method: .setDynamicPublishableKey,
            resolve,
            reject
        )
    }

    @objc func setIsAvailable(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setIsAvailable(
                args as! [String: Any]
            ),
            method: .setIsAvailable,
            resolve,
            reject
        )
    }

    @objc func setIsTracking(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setIsTracking(
                args as! [String: Any]
            ),
            method: .setIsTracking,
            resolve,
            reject
        )
    }

    @objc func setMetadata(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setMetadata(
                args as! [String: Any]
            ),
            method: .setMetadata,
            resolve,
            reject
        )
    }

    @objc func setName(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setName(args as! [String: Any]),
            method: .setName,
            resolve,
            reject
        )
    }

    @objc func setWorkerHandle(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setWorkerHandle(
                args as! [String: Any]
            ),
            method: .setWorkerHandle,
            resolve,
            reject
        )
    }

    private func initListeners() -> Subscriptions {
        let errorsSubscription = HyperTrack.subscribeToErrors { errors in
            self.sendEvent(withName: self.eventErrors, body: serializeErrors(errors))
        }
        let isAvailableSubscription = HyperTrack.subscribeToIsAvailable { [self] isAvailable in
            self.sendEvent(withName: self.eventIsAvailable, body: serializeIsAvailable(isAvailable))
        }
        let isTrackingSubscription = HyperTrack.subscribeToIsTracking { isTracking in
            self.sendEvent(withName: self.eventIsTracking, body: serializeIsTracking(isTracking))
        }
        let locationSubscription = HyperTrack.subscribeToLocation { locationResult in
            self.sendEvent(withName: self.eventLocation, body: serializeLocationResult(locationResult))
        }
        return Subscriptions(locationSubscription: locationSubscription, isAvailableSubscription: isAvailableSubscription, isTrackingSubscription: isTrackingSubscription, errorsSubscription: errorsSubscription)
    }
}

private func sendAsPromise(
    _ result: Result<SuccessResult, FailureResult>,
    method: SDKMethod,
    _ resolve: RCTPromiseResolveBlock,
    _ reject: RCTPromiseRejectBlock
) {
    switch result {
    case let .success(success):
        switch success {
        case .void:
            resolve(nil)
        case let .dict(value):
            resolve(value)
        case let .array(value):
            resolve(value)
        }
    case let .failure(failure):
        switch failure {
        case let .error(message):
            reject(
                "method_call_error",
                "\(method.rawValue): \(message)",
                NSError(domain: "com.hypertrack.sdk.reactnative.ios", code: 0, userInfo: nil)
            )
        case let .fatalError(message):
            preconditionFailure(message)
        }
    }
}
