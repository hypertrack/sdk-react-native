# do not create a shorcut for `publlish` command to avoid accidental publishing
alias b := build
alias cnm := clear-node-modules
alias d := docs
alias gd := get-dependencies
alias od := open-docs

# Source: https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
# \ are escaped
SEMVER_REGEX := "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?"

# MAKE SURE YOU HAVE
# #!/usr/bin/env sh
# set -e
# AT THE TOP OF YOUR RECIPE
_ask-confirm:
  @bash -c 'read confirmation; if [[ $confirmation != "y" && $confirmation != "Y" ]]; then echo "Okay 😮‍💨 😅"; exit 1; fi'

build: get-dependencies docs
    yarn --cwd sdk prepare
    yarn --cwd plugin_android_location_services_google prepare
    yarn --cwd plugin_android_location_services_google_19_0_1 prepare
    yarn --cwd plugin_android_push_service_firebase prepare

clean: clear-node-modules

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

release publish="dry-run": build
    #!/usr/bin/env sh
    set -euo pipefail
    VERSION=$(just version)
    if [ {{publish}} = "publish" ]; then
        BRANCH=$(git branch --show-current)
        if [ $BRANCH != "master" ]; then
            echo "You must be on main branch to publish a new version (current branch: $BRANCH))"
            exit 1
        fi
        echo "Are you sure you want to publish version $VERSION? (y/N)"
        just _ask-confirm
        npm publish sdk
        npm publish plugin_android_location_services_google
        npm publish plugin_android_location_services_google_19_0_1
        npm publish plugin_android_push_service_firebase
        open "https://www.npmjs.com/package/hypertrack-sdk-react-native/v/$VERSION"
    else
        npm publish --dry-run sdk
        npm publish --dry-run plugin_android_location_services_google
        npm publish --dry-run plugin_android_location_services_google_19_0_1
        npm publish --dry-run plugin_android_push_service_firebase
    fi

setup: get-dependencies

version:
    @cat sdk/package.json | grep version | head -n 1 | grep -o -E '{{SEMVER_REGEX}}'
