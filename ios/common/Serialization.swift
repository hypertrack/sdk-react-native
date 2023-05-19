import HyperTrack

private let keyType = "type"
private let keyValue = "value"
private let keyGeotagData = "data"
private let keyExpectedLocation = "expectedLocation"

private let typeSuccess = "success"
private let typeFailure = "failure"
private let typeHyperTrackError = "hyperTrackError"
private let typeIsTracking = "isTracking"
private let typeIsAvailable = "isAvailable"
private let typeLocation = "location"

func getJSON(_ dictionary: [String: Any]) -> HyperTrack.JSON.Object {
    return try! DictionaryDecoder().decode(HyperTrack.JSON.Object.self, from: dictionary)
}

func getHyperTrackError(_ error: HyperTrack.UnrestorableError) -> HyperTrackError {
    switch error {
    case .invalidPublishableKey:
        return HyperTrackError.invalidPublishableKey
    case .motionActivityPermissionsDenied:
        return HyperTrackError.locationPermissionsDenied
    }
}

func getHyperTrackError(_ error: HyperTrack.RestorableError) -> HyperTrackError {
    switch error {
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
    switch error {
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

func serializeErrors(_ errors: Set<HyperTrack.HyperTrackError>) -> [[String: Any]] {
    return errors.map { error in
        serializeHyperTrackError(getHyperTrackError(error))
    }
}

func serializeDeviceID(_ deviceID: String) -> [String: Any] {
    return [
        keyType: "deviceID",
        keyValue: deviceID,
    ]
}

func serializeLocationResult(_ result: Result<HyperTrack.Location, HyperTrack.LocationError>) -> [String: Any] {
    switch result {
    case let .success(success):
        return [
            keyType: typeSuccess,
            keyValue: [
                keyType: typeLocation,
                keyValue: [
                    "latitude": success.latitude,
                    "longitude": success.longitude,
                ],
            ],
        ]
    case let .failure(failure):
        var locationError: [String: Any]
        switch failure {
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
                keyType: "starting",
            ]
        case .notRunning:
            locationError = [
                keyType: "notRunning",
            ]
        }
        return [
            keyType: typeFailure,
            keyValue: locationError,
        ]
    }
}

private func serializeLocation(_ location: HyperTrack.Location) -> [String: Any] {
    return [
        keyType: typeLocation,
        keyValue: [
            "latitude": location.latitude,
            "longitude": location.longitude,
        ],
    ]
}

func serializeLocationResult(_ result: Result<HyperTrack.Location, HyperTrack.NoLocationError>) -> [String: Any] {
    switch result {
    case let .success(success):
        return [
            keyType: typeSuccess,
            keyValue: serializeLocation(success),
        ]
    case let .failure(failure):
        var locationError: [String: Any]
        switch failure {
        case .starting:
            locationError = [
                keyType: "starting",
            ]
        case .notRunning:
            locationError = [
                keyType: "notRunning",
            ]
        case let .errors(errors):
            locationError = [
                keyType: "errors",
                keyValue: serializeErrors(errors),
            ]
        }

        return [
            keyType: typeFailure,
            keyValue: locationError,
        ]
    }
}

func serializeExpectedLocationResult(
    _ result: Result<HyperTrack.LocationWithDeviation, HyperTrack.NoLocationError>
) -> [String: Any] {
    switch result {
    case let .success(success):
        return [
            keyType: typeSuccess,
            keyValue: [
                keyType: "locationWithDeviation",
                keyValue: serializeLocationWithDeviation(success),
            ],
        ]
    case let .failure(failure):
        return [
            keyType: typeFailure,
            keyValue: serializeLocationError(failure),
        ]
    }
}

func serializeLocationError(_ error: HyperTrack.NoLocationError) -> [String: Any] {
    switch error {
    case .starting:
        return [
            keyType: "starting",
        ]
    case .notRunning:
        return [
            keyType: "notRunning",
        ]
    case let .errors(errors):
        return [
            keyType: "errors",
            keyValue: serializeErrors(errors),
        ]
    }
}

func serializeLocationWithDeviation(
    _ locationWithDeviation: HyperTrack.LocationWithDeviation
) -> [String: Any] {
    return [
        "location": serializeLocation(locationWithDeviation.location),
        "deviation": locationWithDeviation.deviation,
    ]
}

func serializeHyperTrackErrorAsLocationError(_ error: HyperTrackError) -> [String: Any] {
    return [
        keyType: "errors",
        keyValue: [
            [
                keyType: typeHyperTrackError,
                keyValue: error.rawValue,
            ],
        ],
    ]
}

func serializeHyperTrackError(_ error: HyperTrackError) -> [String: Any] {
    return [
        keyType: typeHyperTrackError,
        keyValue: error.rawValue,
    ]
}

func serializeIsTracking(_ isTracking: Bool) -> [String: Any] {
    return [
        keyType: typeIsTracking,
        keyValue: isTracking,
    ]
}

func serializeIsAvailable(_ isAvailable: HyperTrack.Availability) -> [String: Any] {
    return [
        keyType: typeIsAvailable,
        keyValue: isAvailable == .available,
    ]
}

func deserializeDeviceName(_ data: [String: Any]) -> Result<String, FailureResult> {
    if data[keyType] as? String != "deviceName" {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? String else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeAvailability(_ data: [String: Any]) -> Result<Bool, FailureResult> {
    if data[keyType] as? String != typeIsAvailable {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? Bool else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeGeotagData(
    _ args: [String: Any]
) -> Result<GeotagData, FailureResult> {
    guard let data = args[keyGeotagData] as? [String: Any] else {
        return .failure(.fatalError(getParseError(args, key: keyGeotagData)))
    }
    if let expectedLocationData = args[keyExpectedLocation] as? [String: Any] {
        let expectedLocation = deserializeLocation(expectedLocationData)
        switch expectedLocation {
        case let .failure(failure):
            return .failure(failure)
        case let .success(expectedLocation):
            return .success(.init(data: data, expectedLocation: expectedLocation))
        }
    } else {
        return .success(.init(data: data, expectedLocation: nil))
    }
}

func deserializeLocation(_ data: [String: Any]) -> Result<HyperTrack.Location, FailureResult> {
    if data[keyType] as? String != typeLocation {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? [String: Any] else {
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
