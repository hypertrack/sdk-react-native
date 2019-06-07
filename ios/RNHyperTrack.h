#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>
@import HyperTrack;

/**
 * Error events
 */
extern NSString* _Nonnull const HyperTrackErrorEvent;

/**
 * Tracking events
 */
extern NSString* _Nonnull const HyperTrackTrackingEvent;

@interface RNHyperTrack : RCTEventEmitter <RCTBridgeModule, HTSDKDelegate>
  
@end
