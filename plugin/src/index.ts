import {
  ConfigPlugin,
  WarningAggregator,
  withAppDelegate,
  withInfoPlist,
  withProjectBuildGradle,
  withPlugins,
  withDangerousMod,
  createRunOncePlugin,
} from "@expo/config-plugins";

import fs from "fs";

const PODFILE = `
require File.join(File.dirname(\`node --print "require.resolve('expo/package.json')"\`), "scripts/autolinking")
require File.join(File.dirname(\`node --print "require.resolve('react-native/package.json')"\`), "scripts/react_native_pods")
require File.join(File.dirname(\`node --print "require.resolve('@react-native-community/cli-platform-ios/package.json')"\`), "native_modules")

require 'json'
podfile_properties = JSON.parse(File.read(File.join(__dir__, 'Podfile.properties.json'))) rescue {}

platform :ios, podfile_properties['ios.deploymentTarget'] || '12.0'
install! 'cocoapods',
  :deterministic_uuids => false

plugin 'cocoapods-user-defined-build-types', {
  verbose: true
}

enable_user_defined_build_types!

target '${process.env.APP_TARGET_NAME ?? "ZeloceChauffeur"}' do
  use_expo_modules!
  config = use_native_modules!

  use_frameworks! :linkage => podfile_properties['ios.useFrameworks'].to_sym if podfile_properties['ios.useFrameworks']

  # Flags change depending on the env values.
  flags = get_default_flags()

  pod "HyperTrack/Objective-C", :build_type => :dynamic_framework
  use_react_native!(
    :path => config[:reactNativePath],
    :hermes_enabled => flags[:hermes_enabled] || podfile_properties['expo.jsEngine'] == 'hermes',
    :fabric_enabled => flags[:fabric_enabled],
    # An absolute path to your application root.
    :app_path => "#{Dir.pwd}/.."
  )

  # Uncomment to opt-in to using Flipper
  # Note that if you have use_frameworks! enabled, Flipper will not work
  #
  # if !ENV['CI']
  #   use_flipper!()
  # end

  post_install do |installer|
    react_native_post_install(installer)
    __apply_Xcode_12_5_M1_post_install_workaround(installer)
  end

  post_integrate do |installer|
    begin
      expo_patch_react_imports!(installer)
    rescue => e
      Pod::UI.warn e
    end
  end

end
`;

const withPodfile: ConfigPlugin = config => {
  return withDangerousMod(config, [
    "ios",
    config => {
      const Podfile_path = `${config.modRequest.projectRoot}/ios/Podfile`;
      fs.writeFile(Podfile_path, PODFILE, err => {
        console.log(err);
      });
      return config;
    },
  ]);
};

const withIosPermissions: ConfigPlugin = config => {
  return withInfoPlist(config, config => {
    config.modResults.NSLocationAlwaysAndWhenInUseUsageDescription =
      "Pour pouvoir utiliser l'application Zeloce et partager votre position avec votre équipe, il faut donner l'accès à la géoloc de cet appareil";
    config.modResults.NSLocationAlwaysUsageDescription =
      "Pour pouvoir utiliser l'application Zeloce et partager votre position avec votre équipe, il faut donner l'accès à la géoloc de cet appareil";
    config.modResults.NSLocationWhenInUseUsageDescription =
      "Pour pouvoir utiliser l'application Zeloce et partager votre position avec votre équipe, il faut donner l'accès à la géoloc de cet appareil";
    config.modResults.NSMotionUsageDescription =
      "Pour pouvoir utiliser l'application Zeloce et partager votre position avec votre équipe, il faut donner l'accès à la géoloc de cet appareil";
    config.modResults.UIBackgroundModes = ["location", "remote-notification"];

    return config;
  });
};

const withAppDelegateUpdates: ConfigPlugin = config => {
  return withAppDelegate(config, config => {
    config.modResults.contents = `
#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import <React/RCTLinkingManager.h>
#import <React/RCTConvert.h>

#import <React/RCTAppSetupUtils.h>

// Added by Expo/plugins for HyperTrack
#import <HyperTrack/HyperTrack-Swift.h>

#if RCT_NEW_ARCH_ENABLED
#import <React/CoreModulesPlugins.h>
#import <React/RCTCxxBridgeDelegate.h>
#import <React/RCTFabricSurfaceHostingProxyRootView.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTSurfacePresenterBridgeAdapter.h>
#import <ReactCommon/RCTTurboModuleManager.h>

#import <react/config/ReactNativeConfig.h>

@interface AppDelegate () <RCTCxxBridgeDelegate, RCTTurboModuleManagerDelegate> {
  RCTTurboModuleManager *_turboModuleManager;
  RCTSurfacePresenterBridgeAdapter *_bridgeAdapter;
  std::shared_ptr<const facebook::react::ReactNativeConfig> _reactNativeConfig;
  facebook::react::ContextContainer::Shared _contextContainer;
}
@end
#endif

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  RCTAppSetupPrepareApp(application);

  RCTBridge *bridge = [self.reactDelegate createBridgeWithDelegate:self launchOptions:launchOptions];

#if RCT_NEW_ARCH_ENABLED
  _contextContainer = std::make_shared<facebook::react::ContextContainer const>();
  _reactNativeConfig = std::make_shared<facebook::react::EmptyReactNativeConfig const>();
  _contextContainer->insert("ReactNativeConfig", _reactNativeConfig);
  _bridgeAdapter = [[RCTSurfacePresenterBridgeAdapter alloc] initWithBridge:bridge contextContainer:_contextContainer];
  bridge.surfacePresenter = _bridgeAdapter.surfacePresenter;
#endif

  UIView *rootView = [self.reactDelegate createRootViewWithBridge:bridge moduleName:@"main" initialProperties:nil];

  rootView.backgroundColor = [UIColor whiteColor];
  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [self.reactDelegate createRootViewController];
  rootViewController.view = rootView;
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];

  [super application:application didFinishLaunchingWithOptions:launchOptions];

  // Added by Expo/plugins for HyperTrack
  [HTSDK registerForRemoteNotifications];

  return YES;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge
{
  // If you'd like to export some custom RCTBridgeModules, add them here!
  return @[];
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

// Linking API
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
  return [super application:application openURL:url options:options] || [RCTLinkingManager application:application openURL:url options:options];
}

// Universal Links
- (BOOL)application:(UIApplication *)application continueUserActivity:(nonnull NSUserActivity *)userActivity restorationHandler:(nonnull void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler {
  BOOL result = [RCTLinkingManager application:application continueUserActivity:userActivity restorationHandler:restorationHandler];
  return [super application:application continueUserActivity:userActivity restorationHandler:restorationHandler] || result;
}

// Explicitly define remote notification delegates to ensure compatibility with some third-party libraries
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  // Added by Expo/plugins for HyperTrack
  [HTSDK didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
  return [super application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

// Explicitly define remote notification delegates to ensure compatibility with some third-party libraries
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
  // Added by Expo/plugins for HyperTrack
  [HTSDK didFailToRegisterForRemoteNotificationsWithError:error];
  return [super application:application didFailToRegisterForRemoteNotificationsWithError:error];
}

// Explicitly define remote notification delegates to ensure compatibility with some third-party libraries
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
{
  // Added by Expo/plugins for HyperTrack
  [HTSDK didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
  return [super application:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
}

#if RCT_NEW_ARCH_ENABLED

#pragma mark - RCTCxxBridgeDelegate

- (std::unique_ptr<facebook::react::JSExecutorFactory>)jsExecutorFactoryForBridge:(RCTBridge *)bridge
{
  _turboModuleManager = [[RCTTurboModuleManager alloc] initWithBridge:bridge
                                                             delegate:self
                                                            jsInvoker:bridge.jsCallInvoker];
  return RCTAppSetupDefaultJsExecutorFactory(bridge, _turboModuleManager);
}

#pragma mark RCTTurboModuleManagerDelegate

- (Class)getModuleClassFromName:(const char *)name
{
  return RCTCoreModulesClassProvider(name);
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const std::string &)name
                                                      jsInvoker:(std::shared_ptr<facebook::react::CallInvoker>)jsInvoker
{
  return nullptr;
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const std::string &)name
                                                     initParams:
                                                         (const facebook::react::ObjCTurboModule::InitParams &)params
{
  return nullptr;
}

- (id<RCTTurboModule>)getModuleInstanceFromClass:(Class)moduleClass
{
  return RCTAppSetupDefaultModuleFromClass(moduleClass);
}

#endif

@end
`;
    return config;
  });
};

const addAndroidMavenRepository = (buildGradle: string) => {
  return `${buildGradle}
    allprojects {
      repositories {
        maven {
          name 'hypertrack'
          url 'https://s3-us-west-2.amazonaws.com/m2.hypertrack.com/'
        }
      }
    }
  `;
};

const withAndroidBuildGradleMods: ConfigPlugin = config => {
  return withProjectBuildGradle(config, config => {
    if (config.modResults.language === "groovy") {
      config.modResults.contents = addAndroidMavenRepository(config.modResults.contents);
    } else {
      WarningAggregator.addWarningAndroid(
        "react-native-firebase-hypertrack",
        `Cannot automatically configure project build.gradle if it's not groovy`,
      );
    }
    return config;
  });
};

const withHypertrack: ConfigPlugin = config => {
  return withPlugins(config, [
    withAndroidBuildGradleMods,
    withAppDelegateUpdates,
    withIosPermissions,
    withPodfile,
  ]);
};

export default createRunOncePlugin(withHypertrack, "hypertrack", "0.0.1");
