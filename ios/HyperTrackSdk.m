#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@interface RCT_EXTERN_MODULE(HyperTrackSdk,RCTEventEmitter)

RCT_EXTERN_METHOD(getDeviceID: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setName:(NSString *)name
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getName: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getMetadata: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setMetadata:(NSDictionary<NSString*, id>*)metadata
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getLocation: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isAvailable: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setAvailability:(BOOL *)availability
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getErrors: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(startTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(stopTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addGeotag:(NSDictionary<NSString*, id>*)metadata
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(sync: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToLocation: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToAvailability: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToErrors: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(cancelSubscription:(NSString *)subscriptionName
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(cancelAllSubscriptions: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

@end

