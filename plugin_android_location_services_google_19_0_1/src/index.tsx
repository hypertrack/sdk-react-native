import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-plugin_android_location_services_google_19_0_1' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const PluginAndroidLocationServicesGoogle1901 = NativeModules.PluginAndroidLocationServicesGoogle1901
  ? NativeModules.PluginAndroidLocationServicesGoogle1901
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
