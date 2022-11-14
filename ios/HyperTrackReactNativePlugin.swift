import HyperTrack

@objc(HyperTrackReactNativePlugin)
class HyperTrackReactNativePlugin: RCTEventEmitter {
    
    private let eventTracking = "onTrackingChanged"
    private let eventAvailability = "onAvailabilityChanged"
    private let eventErrors = "onError"
    
    @objc override static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc override func supportedEvents() -> [String] {
        return [
            eventTracking,
            eventAvailability,
            eventErrors
        ]
    }
    
    @objc override func addListener(_ eventName: String) {
        super.addListener(eventName)
        switch(eventName) {
        case eventTracking:
            sendEvent(withName: eventTracking, body: serializeIsTracking(sdkInstance.isTracking))
        case eventAvailability:
            sendEvent(withName: eventAvailability, body: serializeIsAvailable(sdkInstance.availability))
        case eventErrors:
            // we don't send initial errors value here because errors getter is not implemented for iOS SDK yet
            return
        default:
            return
        }
    }
    
    /// Method name is changed from 'initialize' because RCTEventEmitter has a method with the same name
    @objc func initializeSdk(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.initializeSDK(
                args as! Dictionary<String, Any>
            ).map({ (result:SuccessResult) in
                initListeners()
                return result
            }),
            method: .initialize,
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
            method: .getDeviceId,
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
            hypertrack_sdk_react_native.setName(args as! Dictionary<String, Any>),
            method: .setName,
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
                args as! Dictionary<String, Any>
            ),
            method: .setMetadata,
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
    
    @objc func startTracking(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.startTracking(),
            method: .startTracking,
            resolve,
            reject
        )
    }
    
    @objc func stopTracking(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.stopTracking(),
            method: .stopTracking,
            resolve,
            reject
        )
    }
    
    @objc func setAvailability(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.setAvailability(
                args as! Dictionary<String, Any>
            ),
            method: .setAvailability,
            resolve,
            reject
        )
    }
    
    @objc func isTracking(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.isTracking(),
            method: .isTracking,
            resolve,
            reject
        )
    }
    
    @objc func isAvailable(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.isAvailable(),
            method: .isAvailable,
            resolve,
            reject
        )
    }
    
    @objc func addGeotag(
        _ args: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.addGeotag(
                args as! Dictionary<String, Any>
            ),
            method: .addGeotag,
            resolve,
            reject
        )
    }
    
    @objc func sync(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        sendAsPromise(
            hypertrack_sdk_react_native.sync(),
            method: .sync,
            resolve,
            reject
        )
    }
    
    private func initListeners() {
        NotificationCenter.default.addObserver(self, selector: #selector(onTrackingStarted), name: HyperTrack.startedTrackingNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onTrackingStopped), name: HyperTrack.stoppedTrackingNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onAvailable), name: HyperTrack.becameAvailableNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onUnavailable), name: HyperTrack.becameUnavailableNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onSdkError(notification:)), name: HyperTrack.didEncounterRestorableErrorNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onSdkError(notification:)), name: HyperTrack.didEncounterUnrestorableErrorNotification, object: nil)
    }
    
    @objc
    private func onTrackingStarted() {
        sendTrackingEvent(isTracking: true)
    }
    
    @objc
    private func onTrackingStopped() {
        sendTrackingEvent(isTracking: false)
    }
    
    @objc
    private func onAvailable() {
        sendAvailabilityEvent(isAvailable: .available)
    }
    
    @objc
    private func onUnavailable() {
        sendAvailabilityEvent(isAvailable: .unavailable)
    }
    
    @objc
    private func onSdkError(notification: Notification) {
        let hyperTrackError: HyperTrack.TrackingError? = notification.hyperTrackTrackingError()
        let errors: [Dictionary<String, Any>]!
        switch(hyperTrackError) {
        case .unrestorableError(let unrestorableError):
            errors = [serializeHyperTrackError(getHyperTrackError(unrestorableError))]
        case .restorableError(let restorableError):
            errors = [serializeHyperTrackError(getHyperTrackError(restorableError))]
        default:
            preconditionFailure("onSdkError: Unexpected SDK error \(String(describing: hyperTrackError))")
        }
        sendErrorsEvent(errors)
    }
    
    private func sendTrackingEvent(isTracking: Bool) {
        sendEvent(withName: eventTracking, body: serializeIsTracking(isTracking))
    }
    private func sendAvailabilityEvent(isAvailable: HyperTrack.Availability) {
        sendEvent(withName: eventAvailability, body: serializeIsAvailable(isAvailable))
    }
    
    private func sendErrorsEvent(_ errors: [Dictionary<String, Any>]) {
        sendEvent(withName: eventErrors, body: errors)
    }
    
}

private func sendAsPromise(
    _ result: Result<SuccessResult, FailureResult>,
    method: SDKMethod,
    _ resolve: RCTPromiseResolveBlock,
    _ reject: RCTPromiseRejectBlock
) {
    switch result {
    case .success(let success):
        switch (success) {
        case .void:
            resolve(nil)
        case .dict(let value):
            resolve(value)
        }
    case .failure(let failure):
        switch(failure) {
        case .error(let message):
            reject(
                "method_call_error",
                "\(method.rawValue): \(message)",
                NSError.init(domain: "com.hypertrack.sdk.reactnative.ios", code: 0, userInfo: nil)
            )
        case .fatalError(let message):
            preconditionFailure(message)
        }
    }
}
