import HyperTrack

enum SuccessResult {
    case void
    case dict([String: Any])
    case array([Any])
}

enum FailureResult: Error {
    case fatalError(_ message: String)
    case error(_ message: String)
}

func addGeotag(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    return deserializeGeotagData(args).flatMap { geotagData in
        if let metadata: HyperTrack.JSON.Object = toJSON(geotagData.data) {
            let expectedLocation = geotagData.expectedLocation
            let orderHandle = geotagData.orderHandle
            let orderStatus = geotagData.orderStatus

            if let expectedLocation = geotagData.expectedLocation {
                if orderHandle != nil || orderStatus != nil {
                    if orderHandle == nil || orderStatus == nil {
                        return .failure(.error("orderHandle and orderStatus must be provided"))
                    }
                    let result = HyperTrack.addGeotag(
                        orderHandle: orderHandle!,
                        orderStatus: orderStatus!,
                        metadata: metadata,
                        expectedLocation: expectedLocation
                    )
                    return .success(.dict(serializeLocationWithDeviationResult(result)))
                } else {
                    let result = HyperTrack.addGeotag(metadata, expectedLocation: expectedLocation)
                    return .success(.dict(serializeLocationWithDeviationResult(result)))
                }
            } else {
                if orderHandle != nil || orderStatus != nil {
                    if orderHandle == nil || orderStatus == nil {
                        return .failure(.error("orderHandle and orderStatus must be provided"))
                    }
                    let result = HyperTrack.addGeotag(
                        orderHandle: orderHandle!,
                        orderStatus: orderStatus!,
                        metadata: metadata
                    )
                    return .success(.dict(serializeLocationResult(result)))
                } else {
                    let result = HyperTrack.addGeotag(metadata)
                    return .success(.dict(serializeLocationResult(result)))
                }
            }
        } else {
            return .failure(.error("Failed to deserialize geotag metadata"))
        }
    }
}

func getAllowMockLocation() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeAllowMockLocation(HyperTrack.allowMockLocation)))
}

func getDeviceID() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeDeviceID(HyperTrack.deviceID)))
}

func getDynamicPublishableKey() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeDynamicPublishableKey(HyperTrack.dynamicPublishableKey)))
}

func getErrors() -> Result<SuccessResult, FailureResult> {
    .success(.array(serializeErrors(HyperTrack.errors)))
}

func getIsAvailable() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsAvailable(HyperTrack.isAvailable)))
}

func getIsTracking() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsTracking(HyperTrack.isTracking)))
}

func getLocation() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeLocationResult(HyperTrack.location)))
}

func getMetadata() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeMetadata(HyperTrack.metadata)))
}

func getName() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeName(HyperTrack.name)))
}

func getOrderIsInsideGeofence(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeOrderHandle(args).flatMap { orderHandle in
        if let order = HyperTrack.orders.first(where: { $0.orderHandle == orderHandle }) {
            return .success(.dict(serializeIsInsideGeofence(order.isInsideGeofence)))
        } else {
            return .success(.dict(serializeIsInsideGeofence(.success(false))))
        }
    }
}

func getOrders() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeOrders(Array(HyperTrack.orders))))
}

func getWorkerHandle() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeWorkerHandle(HyperTrack.workerHandle)))
}

func setAllowMockLocation(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeAllowMockLocation(args).flatMap { (allowMockLocation: Bool) in
        HyperTrack.allowMockLocation = allowMockLocation
        return .success(.void)
    }
}

func setDynamicPublishableKey(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeDynamicPublishableKey(args).flatMap { (dynamicPublishableKey: String) in
        HyperTrack.dynamicPublishableKey = dynamicPublishableKey
        return .success(.void)
    }
}

func setIsAvailable(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeIsAvailable(args).flatMap { (isAvailable: Bool) in
        HyperTrack.isAvailable = isAvailable
        return .success(.void)
    }
}

func setIsTracking(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeIsTracking(args).flatMap { (isTracking: Bool) in
        HyperTrack.isTracking = isTracking
        return .success(.void)
    }
}

func setMetadata(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeMetadata(args).flatMap { map in
        if let metadata = toJSON(map) {
            HyperTrack.metadata = metadata
            return .success(.void)
        } else {
            return .failure(.error("Failed to deserialize metadata"))
        }
    }
}

func setName(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeName(args).flatMap { name in
        .success(asVoid(HyperTrack.name = name))
    }
}

func setWorkerHandle(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeWorkerHandle(args).flatMap { workerHandle in
        .success(asVoid(HyperTrack.workerHandle = workerHandle))
    }
}

func asVoid(_: Void) -> SuccessResult {
    .void
}
