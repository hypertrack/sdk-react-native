export interface LocationError {}
export class NotRunning implements LocationError {}
export class Starting implements LocationError {}
export class Errors implements LocationError {
    errors: Set<HyperTrackError>;
 
    constructor(errors: Set<HyperTrackError>) {
      this.errors = errors;
    }
}
