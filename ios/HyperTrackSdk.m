#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>

@interface RCT_EXTERN_MODULE(HyperTrackSdk,RCTEventEmitter)

RCT_EXTERN_METHOD(increment: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(decrement: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getDeviceID: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(startTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(stopTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getLocation: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(availability: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToLocation: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToIsTracking: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeToErrors: (RCTPromiseResolveBlock)resolve reject: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setDeviceName:(NSString *)deviceName
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(cancelSubscription:(NSString *)subscriptionName
                  resolve: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_REMAP_METHOD(multiply,
                 multiplyWithA:(nonnull NSNumber*)a withB:(nonnull NSNumber*)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  NSNumber *result = @([a floatValue] * [b floatValue]);

  resolve(result);
}

@end

