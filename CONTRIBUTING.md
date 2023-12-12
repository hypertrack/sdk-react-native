# Contributing

## FAQ

### How to update the HyperTrack SDK version and make a release?

1. Update SDK version constant

   - Android
     - Change `<plugin name>_HyperTrackSDKVersion` in:
       - [sdk/android/gradle.properties](sdk/android/gradle.properties)
       - [plugin_android_location_services_google/android/gradle.properties](plugin_android_location_services_google/android/gradle.properties)
       - [plugin_android_location_services_google_19_0_1/android/gradle.properties](plugin_android_location_services_google_19_0_1/android/gradle.properties)
       - [plugin_android_push_service_firebase/android/gradle.properties](plugin_android_push_service_firebase/android/gradle.properties)
   - iOS
     - Change `s.dependency 'HyperTrack', '**version**'` in:
       - [sdk/hypertrack-sdk-react-native.podspec](sdk/hypertrack-sdk-react-native.podspec)

2. Increment wrapper version

Change `version` value in:

- `hypertrack-sdk-react-native` version in [sdk/package.json](sdk/package.json)
- `hypertrack-sdk-react-native-plugin-android-location-services-google` in [plugin_android_location_services_google/package.json](plugin_android_location_services_google/package.json)
- `hypertrack-sdk-react-native-plugin-android-location-services-google-19-0-1` in [plugin_android_location_services_google_19_0_1/package.json](plugin_android_location_services_google_19_0_1/package.json)
- `hypertrack-sdk-react-native-plugin-android-push-service-firebase` in [plugin_android_push_service_firebase/package.json](plugin_android_push_service_firebase/package.json)

3. Update [CHANGELOG](CHANGELOG.md)

   - **Add the release link to the bottom**

4. Update badge in [README](README.md)

5. Do the release dry run with `just release` and verify that the release is correct

6. Commit and merge to master

7. Create a Github repo release

   - Release title should be the current version tag

8. Run `npm publish` for each plugin to publish the package to npm:

```
npm publish sdk
npm publish plugin_android_location_services_google
npm publish plugin_android_location_services_google_19_0_1
npm publish plugin_android_push_service_firebase
```

### How to change build config

#### Android

Change Android build config in `gradle.properties` of respective plugins

## Development workflow

To get started with the project, run `yarn` in the root directory to install the required dependencies for each package:

```sh
yarn
```

> While it's possible to use [`npm`](https://github.com/npm/cli), the tooling is built around [`yarn`](https://classic.yarnpkg.com/), so you'll have an easier time if you use `yarn` for development.

Make sure your code passes TypeScript and ESLint. Run the following to verify:

```sh
yarn typescript
yarn lint
```

To fix formatting errors, run the following:

```sh
yarn lint --fix
```

### Linting and tests

[ESLint](https://eslint.org/), [Prettier](https://prettier.io/), [TypeScript](https://www.typescriptlang.org/)

We use [TypeScript](https://www.typescriptlang.org/) for type checking, [ESLint](https://eslint.org/) with [Prettier](https://prettier.io/) for linting and formatting the code, and [Jest](https://jestjs.io/) for testing.

### Sending a pull request

> **Working on your first pull request?** You can learn how from this _free_ series: [How to Contribute to an Open Source Project on GitHub](https://app.egghead.io/playlists/how-to-contribute-to-an-open-source-project-on-github).

When you're sending a pull request:

- Prefer small pull requests focused on one change.
- Verify that linters and tests are passing.
- Review the documentation to make sure it looks good.
- Follow the pull request template when opening a pull request.
- For pull requests that change the API or implementation, discuss with maintainers first by opening an issue.
