import HyperTrack

private let keyExpectedLocation = "expectedLocation"
private let keyGeotagData = "data"
private let keyType = "type"
private let keyValue = "value"

private let typeError = "error"
private let typeFailure = "failure"
private let typeIsAvailable = "isAvailable"
private let typeIsTracking = "isTracking"
private let typeLocation = "location"
private let typeMetadata = "metadata"
private let typeName = "name"
private let typeSuccess = "success"

func deserializeDynamicPublishableKey(
    _ args: [String: Any]
) -> Result<String, FailureResult> {
    if args[keyType] as? String != "dynamicPublishableKey" {
        return .failure(.fatalError(getParseError(args, key: keyType)))
    }
    guard let value = args[keyValue] as? String else {
        return .failure(.fatalError(getParseError(args, key: keyValue)))
    }
    return .success(value)
}

func deserializeGeotagData(
    _ args: [String: Any]
) -> Result<GeotagData, FailureResult> {
    guard let data = args[keyGeotagData] as? [String: Any] else {
        return .failure(.fatalError(getParseError(args, key: keyGeotagData)))
    }
    let orderHandleData = args["orderHandle"] as? [String: Any]
    let orderHandleResult: Result<HyperTrack.OrderHandle, FailureResult>? = if let orderHandleData = orderHandleData {
        deserializeOrderHandle(orderHandleData)
    } else {
        nil
    }
    if case let .failure(failure) = orderHandleResult {
        return .failure(failure)
    }
    let orderHandle: HyperTrack.OrderHandle? = if case let .success(orderHandle) = orderHandleResult {
        orderHandle
    } else {
        nil
    }

    let orderStatusData = args["orderStatus"] as? [String: Any]
    let orderStatusResult: Result<HyperTrack.OrderStatus, FailureResult>? = if let orderStatusData = orderStatusData {
        deserializeOrderStatus(orderStatusData)
    } else {
        nil
    }
    if case let .failure(failure) = orderStatusResult {
        return .failure(failure)
    }
    let orderStatus: HyperTrack.OrderStatus? = if case let .success(orderStatus) = orderStatusResult {
        orderStatus
    } else {
        nil
    }

    if let expectedLocationData = args[keyExpectedLocation] as? [String: Any] {
        let expectedLocation = deserializeLocation(expectedLocationData)
        switch expectedLocation {
        case let .failure(failure):
            return .failure(failure)
        case let .success(expectedLocation):
            return .success(.init(
                data: data,
                expectedLocation: expectedLocation,
                orderHandle: orderHandle,
                orderStatus: orderStatus
            ))
        }
    } else {
        return .success(.init(
            data: data,
            expectedLocation: nil,
            orderHandle: orderHandle,
            orderStatus: orderStatus
        ))
    }
}

func deserializeIsAvailable(_ data: [String: Any]) -> Result<Bool, FailureResult> {
    if data[keyType] as? String != typeIsAvailable {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? Bool else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeIsTracking(_ data: [String: Any]) -> Result<Bool, FailureResult> {
    if data[keyType] as? String != typeIsTracking {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? Bool else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
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
    return .success(.init(latitude: latitude, longitude: longitude))
}

func deserializeMetadata(_ data: [String: Any]) -> Result<[String: Any], FailureResult> {
    if data[keyType] as? String != typeMetadata {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? [String: Any] else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeName(_ data: [String: Any]) -> Result<String, FailureResult> {
    if data[keyType] as? String != typeName {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    guard let value = data[keyValue] as? String else {
        return .failure(.fatalError(getParseError(data, key: keyValue)))
    }
    return .success(value)
}

func deserializeOrderStatus(_ data: [String: Any]) -> Result<HyperTrack.OrderStatus, FailureResult> {
    guard let type = data[keyType] as? String else {
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
    switch type {
    case "orderStatusClockIn":
        return .success(.clockIn)
    case "orderStatusClockOut":
        return .success(.clockOut)
    case "orderStatusCustom":
        guard let value = data[keyValue] as? String else {
            return .failure(.fatalError(getParseError(data, key: keyValue)))
        }
        return .success(.custom(value))
    default:
        return .failure(.fatalError(getParseError(data, key: keyType)))
    }
}

func serializeDeviceID(_ deviceID: String) -> [String: Any] {
    return [
        keyType: "deviceID",
        keyValue: deviceID,
    ]
}

func serializeDynamicPublishableKey(_ dynamicPublishableKey: String) -> [String: Any] {
    return [
        keyType: "dynamicPublishableKey",
        keyValue: dynamicPublishableKey,
    ]
}

func serializeError(_ error: HyperTrack.Error) -> [String: Any] {
    let value: String
    switch error {
    case .blockedFromRunning:
        value = "blockedFromRunning"
    case .invalidPublishableKey:
        value = "invalidPublishableKey"
    case let .location(location):
        switch location {
        case .mocked:
            value = "location.mocked"
        case .servicesDisabled:
            value = "location.servicesDisabled"
        case .signalLost:
            value = "location.signalLost"
        }
    case let .permissions(permissionType):
        switch permissionType {
        case let .location(permission):
            switch permission {
            case .denied:
                value = "permissions.location.denied"
            case .insufficientForBackground:
                value = "permissions.location.insufficientForBackground"
            case .notDetermined:
                value = "permissions.location.notDetermined"
            case .provisional:
                value = "permissions.location.provisional"
            case .reducedAccuracy:
                value = "permissions.location.reducedAccuracy"
            case .restricted:
                value = "permissions.location.restricted"
            }
        }
    }

    return [
        keyType: typeError,
        keyValue: value,
    ]
}

func serializeIsAvailable(_ isAvailable: Bool) -> [String: Any] {
    return [
        keyType: typeIsAvailable,
        keyValue: isAvailable,
    ]
}

func serializeIsTracking(_ isTracking: Bool) -> [String: Any] {
    return [
        keyType: typeIsTracking,
        keyValue: isTracking,
    ]
}

func serializeLocateResult(_ locateResult: Result<HyperTrack.Location, Set<HyperTrack.Error>>) -> [String: Any] {
    switch locateResult {
    case let .success(success):
        return [
            keyType: typeSuccess,
            keyValue: serializeLocation(success),
        ]
    case let .failure(failure):
        return [
            keyType: typeFailure,
            keyValue: serializeErrors(failure),
        ]
    }
}

func serializeLocation(_ location: HyperTrack.Location) -> [String: Any] {
    return [
        keyType: typeLocation,
        keyValue: [
            "latitude": location.latitude,
            "longitude": location.longitude,
        ],
    ]
}

func serializeLocationError(_ error: HyperTrack.Location.Error) -> [String: Any] {
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

func serializeLocationResult(_ result: Result<HyperTrack.Location, HyperTrack.Location.Error>) -> [String: Any] {
    switch result {
    case let .success(success):
        return [
            keyType: typeSuccess,
            keyValue: serializeLocation(success),
        ]
    case let .failure(failure):
        return [
            keyType: typeFailure,
            keyValue: serializeLocationError(failure),
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

func serializeLocationWithDeviationResult(
    _ result: Result<HyperTrack.LocationWithDeviation, HyperTrack.Location.Error>
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

func serializeMetadata(_ metadata: HyperTrack.JSON.Object) -> [String: Any] {
    return [
        keyType: typeMetadata,
        keyValue: toMap(metadata),
    ]
}

func serializeName(_ name: String) -> [String: Any] {
    return [
        keyType: typeName,
        keyValue: name,
    ]
}

func serializeErrors(_ errors: Set<HyperTrack.Error>) -> [[String: Any]] {
    return errors.map { error in
        serializeError(error)
    }
}

private func toMap(_ object: HyperTrack.JSON.Object) -> [String: Any] {
    return object.mapValues { value in
        switch value {
        case let .object(objectValue):
            return toMap(objectValue) as Any
        case let .array(arr):
            return toArray(arr)
        case let .bool(bool):
            return bool
        case let .number(number):
            return number
        case let .string(string):
            return string
        case .null:
            return NSNull()
        }
    }
}

private func toArray(_ array: [HyperTrack.JSON]) -> [Any] {
    return array.map { (json: HyperTrack.JSON) -> Any in
        switch json {
        case let .object(objectValue):
            return toMap(objectValue)
        case let .array(arr):
            return toArray(arr)
        case let .bool(bool):
            return bool
        case let .number(number):
            return number
        case let .string(string):
            return string
        case .null:
            return NSNull()
        }
    }
}

private func getParseError(_ data: Any, key: String) -> String {
    return "Invalid input for key \(key): \(data)"
}
