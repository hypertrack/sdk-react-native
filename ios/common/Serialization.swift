import HyperTrack


private let keyType = "type"
private let keyValue = "value"
private let keyExpectedLocation = "expectedLocation"

private let typeSuccess = "success"
private let typeFailure = "failure"
private let typeHyperTrackError = "hyperTrackError"
private let typeIsTracking = "isTracking"
private let typeIsAvailable = "isAvailable"
private let typeGeotagSuccess = "geotagSuccess"
private let typeGeotagFailure = "geotagFailure"
private let typeGeotagSuccessWithDeviation = "geotagSuccessWithDeviation"
private let typeLocation = "location"

let keyGeotagData = "data"

func getJSON(_ dictionary: Dictionary<String, Any>) -> HyperTrack.JSON.Object {
    return try! DictionaryDecoder().decode(HyperTrack.JSON.Object.self, from: dictionary)
}

func getHyperTrackError(_ error: HyperTrack.UnrestorableError) -> HyperTrackError {
    switch(error) {
    case .invalidPublishableKey:
        return HyperTrackError.invalidPublishableKey
    case .motionActivityPermissionsDenied:
        return HyperTrackError.locationPermissionsDenied
    }
}

func getHyperTrackError(_ error: HyperTrack.RestorableError) -> HyperTrackError {
    switch(error) {
    case .locationPermissionsNotDetermined:
        return HyperTrackError.locationPermissionsNotDetermined
    case .motionActivityPermissionsNotDetermined:
        return HyperTrackError.motionActivityPermissionsNotDetermined
    case .locationPermissionsCantBeAskedInBackground:
        return HyperTrackError.locationPermissionsNotDetermined
    case .motionActivityPermissionsCantBeAskedInBackground:
        return HyperTrackError.motionActivityPermissionsNotDetermined
    case .locationPermissionsRestricted:
        return HyperTrackError.locationPermissionsRestricted
    case .motionActivityPermissionsRestricted:
        return HyperTrackError.motionActivityPermissionsRestricted
    case .locationPermissionsDenied:
        return HyperTrackError.locationPermissionsDenied
    case .locationPermissionsInsufficientForBackground:
        return HyperTrackError.locationPermissionsInsufficientForBackground
    case .locationServicesDisabled:
        return HyperTrackError.locationServicesDisabled
    case .motionActivityServicesDisabled:
        return HyperTrackError.motionActivityServicesDisabled
    case .networkConnectionUnavailable:
        return HyperTrackError.networkConnectionUnavailable
    case .trialEnded:
        return HyperTrackError.blockedFromRunning
    case .paymentDefault:
        return HyperTrackError.blockedFromRunning
    }
}

func getHyperTrackError(_ error: HyperTrack.HyperTrackError) -> HyperTrackError {
    switch(error) {
    case .locationPermissionsNotDetermined:
        return .locationPermissionsNotDetermined
    case .locationPermissionsInsufficientForBackground:
        return .locationPermissionsInsufficientForBackground
    case .locationPermissionsRestricted:
        return .locationPermissionsRestricted
    case .locationPermissionsReducedAccuracy:
        return .locationPermissionsReducedAccuracy
    case .locationPermissionsProvisional:
        return .locationPermissionsProvisional
    case .locationPermissionsDenied:
        return .locationPermissionsDenied
    case .locationServicesDisabled:
        return .locationServicesDisabled
    case .motionActivityPermissionsNotDetermined:
        return .motionActivityPermissionsNotDetermined
    case .motionActivityPermissionsDenied:
        return .motionActivityPermissionsDenied
    case .motionActivityServicesDisabled:
        return .motionActivityServicesDisabled
    case .motionActivityServicesUnavailable:
        return .motionActivityServicesUnavailable
    case .gpsSignalLost:
        return .gpsSignalLost
    case .locationMocked:
        return .locationMocked
    case .invalidPublishableKey:
        return .invalidPublishableKey
    case .blockedFromRunning:
        return .blockedFromRunning
    default:
        preconditionFailure("Unknown error \(error)")
    }
}

func serializeErrors(_ errors: Set<HyperTrack.HyperTrackError>) -> Array<Dictionary<String, Any>> {
    return errors.map({ error in
        serializeHyperTrackError(getHyperTrackError(error))
    })
}

func serializeDeviceID(_ deviceID: String) -> Dictionary<String, Any> {
    return [
        keyType: "deviceID",
        keyValue: deviceID
    ]
}

func serializeLocationResult(_ result: Result<HyperTrack.Location, HyperTrack.LocationError>) -> Dictionary<String, Any>  {
    switch (result) {
    case .success(let success):
        return [
            keyType: typeSuccess,
            keyValue: [
                keyType: typeLocation,
                keyValue: [
                    "latitude": success.latitude,
                    "longitude": success.longitude
                ]
            ]
        ]
    case .failure(let failure):
        var locationError: Dictionary<String, Any>
        switch(failure) {
        case .locationPermissionsNotDetermined:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsNotDetermined)
        case .locationPermissionsCantBeAskedInBackground:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsNotDetermined)
        case .locationPermissionsInsufficientForBackground:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsInsufficientForBackground)
        case .locationPermissionsRestricted:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsRestricted)
        case .locationPermissionsReducedAccuracy:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsReducedAccuracy)
        case .locationPermissionsDenied:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationPermissionsDenied)
        case .locationServicesDisabled:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationServicesDisabled)
        case .motionActivityPermissionsNotDetermined:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.motionActivityPermissionsNotDetermined)
        case .motionActivityPermissionsCantBeAskedInBackground:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.motionActivityPermissionsNotDetermined)
        case .motionActivityPermissionsDenied:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.motionActivityPermissionsDenied)
        case .motionActivityServicesDisabled:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.motionActivityServicesDisabled)
        case .gpsSignalLost:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.gpsSignalLost)
        case .locationMocked:
            locationError = serializeHyperTrackErrorAsLocationError(HyperTrackError.locationMocked)
        case .starting:
            locationError = [
                keyType: "starting"
            ]
        case .notRunning:
            locationError = [
                keyType: "notRunning"
            ]
        }
        return [
            keyType: typeFailure,
            keyValue: locationError
        ]
    }
}

private func serializeLocation(_ location: HyperTrack.Location) -> Dictionary<String, Any> {
    return [
        keyType: typeLocation,
        keyValue: [
            "latitude": location.latitude,
            "longitude": location.longitude
        ]
    ]
}

func serializeLocationResult(_ result: Result<HyperTrack.Location, HyperTrack.NoLocationError>) -> Dictionary<String, Any>  {
    switch (result) {
    case .success(let success):
        return [
            keyType: typeSuccess,
            keyValue: serializeLocation(success)
        ]
    case .failure(let failure):
        var locationError: Dictionary<String, Any>
        switch(failure) {
        case .starting:
            locationError = [
                keyType: "starting"
            ]
        case .notRunning:
            locationError = [
                keyType: "notRunning"
            ]
        case .errors(let errors):
            locationError = [
                keyType: "errors",
                keyValue: serializeErrors(errors)
            ]
        }

        return [
            keyType: typeFailure,
            keyValue: locationError
        ]
    }
}

func serializeGeotagResult(_ result: GeotagResultData) -> Dictionary<String, Any> {
    switch (result) {
        case .geotagSuccess(let location):
            return [
                keyType: typeGeotagSuccess,
                keyValue: serializeLocation(location)
            ]
        case .geotagFailure(let error):
            return [
                keyType: typeGeotagFailure,
                keyValue: serializeHyperTrackError(getHyperTrackError(error))
            ]
        case .geotagSuccessWithDeviation(let location, let deviation):
            return [
                keyType: typeGeotagSuccessWithDeviation,
                keyValue: [
                    typeLocation: serializeLocation(location),
                    "deviation": deviation
                ]
            ]
    }
}

func serializeHyperTrackErrorAsLocationError(_ error: HyperTrackError) -> Dictionary<String, Any> {
    return [
        keyType: "errors",
        keyValue: [
            [
                keyType: typeHyperTrackError,
                keyValue: error.rawValue
            ]
        ]
    ]
}

func serializeHyperTrackError(_ error: HyperTrackError) -> Dictionary<String, Any> {
    return [
        keyType: typeHyperTrackError,
        keyValue: error.rawValue
    ]
}

func serializeIsTracking(_ isTracking: Bool) -> Dictionary<String, Any> {
    return [
        keyType: typeIsTracking,
        keyValue: isTracking
    ]
}

func serializeIsAvailable(_ isAvailable: HyperTrack.Availability) -> Dictionary<String, Any> {
    return [
        keyType: typeIsAvailable,
        keyValue: isAvailable == .available
    ]
}

func deserializeDeviceName(_ data: Dictionary<String, Any>) -> Result<String, FailureResult> {
    if (data[keyType] as? String != "deviceName") {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? String else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeAvailability(_ data: Dictionary<String, Any>) -> Result<Bool, FailureResult> {
    if (data[keyType] as? String != typeIsAvailable) {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? Bool else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeGeotagData(
    _ geotag: Dictionary<String, Any>
) -> Result<Geotag, FailureResult> {
    guard let data = geotag[keyGeotagData] as? Dictionary<String, Any> else {
        return .failure(.fatalError(getParseError(geotag, key: keyGeotagData)))
    }
    let expectedLocationData = geotag[keyExpectedLocation] as? Dictionary<String, Any>
    let expectedLocation = expectedLocationData.flatMap { data in
        return deserializeLocation(data).value
    }
    return .success(Geotag(data: data, expectedLocation: expectedLocation))
}

func deserializeLocation(_ data: Dictionary<String, Any>) -> Result<HyperTrack.Location, FailureResult> {
    if (data[keyType] as? String != typeLocation) {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? Dictionary<String, Any> else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    guard let latitude = value["latitude"] as? Double else {
        return .failure(.fatalError(getParseError(value, key: "latitude")))
    }
    guard let longitude = value["longitude"] as? Double else {
        return .failure(.fatalError(getParseError(value, key: "longitude")))
    }
    return .success(HyperTrack.Location(latitude: latitude, longitude: longitude))
}

func getParseError(_ data: Any, key: String) -> String {
    return "Invalid input for key \(key): \(data)"
}
