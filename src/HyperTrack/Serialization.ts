function deserializeTracking(isTracking: Object): boolean {
    if(isTracking.get("type")! == "isTracking") {
        return isTracking.get("value") as boolean
    } else {
        throw Error("invalid isTracking response")
    }
}

function deserializeAvailability(isAvailable: Object): boolean {
    if(isAvailable.get("type")! == "isAvailable") {
        return isAvailable.get("value") as boolean
    } else {
        throw Error("invalid isAvailable response")
    }
}

function deserializeLocationResponse(response: Object): (LocationError | Location) {
    throw Error("not implemented")
}