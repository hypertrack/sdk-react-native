public struct SDKInitParams {
    let publishableKey: String
    let loggingEnabled: Bool
    let allowMockLocations: Bool
    let automaticallyRequestPermissions: Bool
    
    init?(_ map: Dictionary<String, Any>) {
        self.publishableKey = (map["publishableKey"] as! String)
        self.loggingEnabled = (map["loggingEnabled"] as! Bool)
        self.allowMockLocations = (map["allowMockLocations"] as! Bool)
        self.automaticallyRequestPermissions = (map["automaticallyRequestPermissions"] as! Bool)
    }
}
