import HyperTrack

struct GeotagData {
    let data: [String: Any]
    let expectedLocation: HyperTrack.Location?
    let orderHandle: String?
    let orderStatus: HyperTrack.OrderStatus?

    init(
        data: [String: Any],
        expectedLocation: HyperTrack.Location?,
        orderHandle: String?,
        orderStatus: HyperTrack.OrderStatus?
    ) {
        self.data = data
        self.expectedLocation = expectedLocation
        self.orderHandle = orderHandle
        self.orderStatus = orderStatus
    }
}
