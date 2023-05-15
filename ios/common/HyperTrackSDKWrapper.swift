import HyperTrack

enum SuccessResult {
    case void
    case dict(Dictionary<String, Any>)
}

enum FailureResult: Error {
    case fatalError(_ message: String)
    case error(_ message: String)
}

var sdkInstance: HyperTrack! // initialize method is guaranteed to be called (by non-native side) prior to any access to the SDK instance

// method is named 'initializeSDK' to avoid conflict with React Native plugin 'initialize' method
func initializeSDK(
    _ params: Dictionary<String, Any>
) -> Result<SuccessResult, FailureResult> {
    if let sdkInitParams = SDKInitParams(params) {
        let publishableKey = HyperTrack.PublishableKey(params["publishableKey"] as! String)
        if let publishableKey = publishableKey {
            switch HyperTrack.makeSDK(
                publishableKey: publishableKey,
                automaticallyRequestPermissions: sdkInitParams.automaticallyRequestPermissions,
                mockLocationsAllowed: sdkInitParams.allowMockLocations
            ) {
            case let .success(hyperTrack):
                sdkInstance = hyperTrack
                HyperTrack.isLoggingEnabled = sdkInitParams.loggingEnabled
                return .success(.void)
            case let .failure(fatalError):
                return .failure(mapFatalError(fatalError: fatalError))
            }
        } else {
            return .failure(.fatalError("Invalid publishable key"))
        }
    } else {
        return .failure(.error("Invalid SDK init parameters \(params)"))
    }
}

func getDeviceID() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeDeviceID(sdkInstance.deviceID)))
}

func getLocation() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeLocationResult(sdkInstance.location)))
}

func startTracking() -> Result<SuccessResult, FailureResult> {
    .success(asVoid(sdkInstance.start()))
}

func stopTracking() -> Result<SuccessResult, FailureResult> {
    .success(asVoid(sdkInstance.stop()))
}

func setAvailability(
    _ availability: Dictionary<String, Any>
) -> Result<SuccessResult, FailureResult> {
    deserializeAvailability(availability).flatMap { (isAvailable:Bool) in
        if(isAvailable) {
            sdkInstance.availability = .available
        } else {
            sdkInstance.availability = .unavailable
        }
        return .success(.void)
    }
}

func setName(_ args: Dictionary<String, Any>) -> Result<SuccessResult, FailureResult> {
    deserializeDeviceName(args).flatMap({ (name: String) in
        .success(asVoid(sdkInstance.setDeviceName(name)))
    })
}

func setMetadata(_ map: Dictionary<String, Any>) -> Result<SuccessResult, FailureResult> {
    sdkInstance.metadata = getJSON(map)
    return .success(.void)
}

func isTracking() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsTracking(sdkInstance.isTracking)))
}

func isAvailable() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsAvailable(sdkInstance.availability)))
}

func addGeotag(_ args: Dictionary<String, Any>) -> Result<SuccessResult, FailureResult> {
    return deserializeGeotagData(args).flatMap { geotagData in
        let metadata: HyperTrack.JSON.Object = getJSON(geotagData.data)
        if let expectedLocation = geotagData.expectedLocation {
            let result = sdkInstance.addGeotag(metadata, expectedLocation: expectedLocation)
            return .success(.dict(serializeExpectedLocationResult(result)))
        } else {
            let result = sdkInstance.addGeotag(metadata)
            return .success(.dict(serializeLocationResult(result)))
        }
    }
}

func sync() -> Result<SuccessResult, FailureResult> {
    .success(asVoid(sdkInstance.syncDeviceSettings()))
}

func asVoid(_ void: Void) -> SuccessResult {
    .void
}

private func mapFatalError(fatalError: HyperTrack.FatalError) -> FailureResult {
    switch fatalError {
    case .developmentError(.missingLocationUpdatesBackgroundModeCapability):
        return .fatalError("missingLocationUpdatesBackgroundModeCapability")
    case .developmentError(.runningOnSimulatorUnsupported):
        return .fatalError("runningOnSimulatorUnsupported")
    // typo is in the HyperTrack SDK itself, no need to fix (it will be deleted in the next major update)
    case .productionError(.locationServicesUnavalible):
        return .error("locationServicesUnavalible")
    case .productionError(.motionActivityServicesUnavalible):
        return .error("motionActivityServicesUnavalible")
    case .productionError(.motionActivityPermissionsDenied):
        return .fatalError("motionActivityPermissionsDenied")
    }
}
