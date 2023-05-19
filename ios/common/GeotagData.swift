import HyperTrack

struct GeotagData {
    let data: [String: Any]
    let expectedLocation: HyperTrack.Location?

    init(data: [String: Any], expectedLocation: HyperTrack.Location?) {
        self.data = data
        self.expectedLocation = expectedLocation
    }
}
