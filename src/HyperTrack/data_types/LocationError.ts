type NotRunning = {
  type: "notRunning"
}
type Starting = {
  type: "starting"
}
type Errors = {
  type: "errors"
  errors: HyperTrackError[]
};

type LocationError = NotRunning | Starting | Errors
