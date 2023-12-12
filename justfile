# do not create a shorcut for `publlish` command to avoid accidental publishing
alias b := build
alias cnm := clear-node-modules
alias d := docs
alias gd := get-dependencies
alias od := open-docs
alias r := release

# Source: https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
# \ are escaped
SEMVER_REGEX := "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?"


build: docs
    yarn -cwd sdk prepare
    yarn -cwd plugin_android_location_services_google prepare
    yarn -cwd plugin_android_location_services_google_19_0_1 prepare
    yarn -cwd plugin_android_push_service_firebase prepare

clear-node-modules:
    rm -rf sdk/node_modules
    rm -rf plugin_android_location_services_google/node_modules
    rm -rf plugin_android_location_services_google_19_0_1/node_modules
    rm -rf plugin_android_push_service_firebase/node_modules

docs:
    yarn --cwd sdk docs

get-dependencies:
    yarn --cwd sdk
    yarn --cwd plugin_android_location_services_google
    yarn --cwd plugin_android_location_services_google_19_0_1
    yarn --cwd plugin_android_push_service_firebase

open-docs: docs
    open docs/index.html

publish:
    #!/usr/bin/env sh
    VERSION=$(just version)
    npm publish sdk
    npm publish plugin_android_location_services_google
    npm publish plugin_android_location_services_google_19_0_1
    npm publish plugin_android_push_service_firebase
    open "https://www.npmjs.com/package/hypertrack-sdk-react-native/v/$VERSION"
    open "https://www.npmjs.com/package/hypertrack-plugin-android-location-services-google/v/$VERSION"
    open "https://www.npmjs.com/package/hypertrack-plugin-android-location-services-google-19-0-1/v/$VERSION"
    open "https://www.npmjs.com/package/hypertrack-plugin-android-push-service-firebase/v/$VERSION"

release:
    npm publish sdk --dry-run
    npm publish plugin_android_location_services_google --dry-run
    npm publish plugin_android_location_services_google_19_0_1 --dry-run
    npm publish plugin_android_push_service_firebase --dry-run

version:
    @cat sdk/package.json | grep version | head -n 1 | grep -o -E '{{SEMVER_REGEX}}'
