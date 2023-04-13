import HyperTrack

enum GeotagResultData {
    case geotagSuccess(location: HyperTrack.Location)
    case geotagFailure(error: Error)
    case geotagSuccessWithDeviation(location: HyperTrack.Location, deviation: Double)
}
