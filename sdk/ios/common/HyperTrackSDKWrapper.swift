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
            if let expectedLocation = geotagData.expectedLocation {
                let result = HyperTrack.addGeotag(metadata, expectedLocation: expectedLocation)
                return .success(.dict(serializeLocationWithDeviationResult(result)))
            } else {
                let result = HyperTrack.addGeotag(metadata)
                return .success(.dict(serializeLocationResult(result)))
            }
        } else {
            return .failure(.error("Failed to deserialize geotag metadata"))
        }
    }
}

func getDeviceID() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeDeviceID(HyperTrack.deviceID)))
}

func getErrors() -> Result<SuccessResult, FailureResult> {
    .success(.array(serializeErrors(HyperTrack.errors)))
}

func getMetadata() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeMetadata(HyperTrack.metadata)))
}

func getName() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeName(HyperTrack.name)))
}

func getLocation() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeLocationResult(HyperTrack.location)))
}

func getIsAvailable() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsAvailable(HyperTrack.isAvailable)))
}

func setIsAvailable(_ args: [String: Any]) -> Result<SuccessResult, FailureResult> {
    deserializeIsAvailable(args).flatMap { (isAvailable: Bool) in
        HyperTrack.isAvailable = isAvailable
        return .success(.void)
    }
}

func getIsTracking() -> Result<SuccessResult, FailureResult> {
    .success(.dict(serializeIsTracking(HyperTrack.isTracking)))
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

func asVoid(_: Void) -> SuccessResult {
    .void
}
