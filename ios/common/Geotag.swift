import HyperTrack

struct Geotag {
    val data: Dictionary<String, Any>
    val expectedLocation: HyperTrack.Location,

    constructor(data: Dictionary<String, Any>, expectedLocation: HyperTrack.Location) {
        this.data = data
        this.expectedLocation = expectedLocation
    }
}