alias b := build
alias d := docs
alias gd := get-dependencies
alias od := open-docs
alias r := release

build: docs
    yarn -cwd sdk prepare
    yarn -cwd plugin_android_location_services_google prepare
    yarn -cwd plugin_android_location_services_google_19_0_1 prepare
    yarn -cwd plugin_android_push_service_firebase prepare

docs:
    yarn --cwd sdk docs

get-dependencies:
    yarn --cwd sdk
    yarn --cwd plugin_android_location_services_google
    yarn --cwd plugin_android_location_services_google_19_0_1
    yarn --cwd plugin_android_push_service_firebase

open-docs: docs
    open docs/index.html

release guard="dry-run": docs
    #!/usr/bin/env sh
    if [ "{{guard}}" = "publish" ]; then \
        echo "Publishing 'sdk'"
        # npm publish sdk
        echo "Publishing 'plugin_android_location_services_google'"
        # npm publish plugin_android_location_services_google
        echo "Publishing 'plugin_android_location_services_google_19_0_1'"
        # npm publish plugin_android_location_services_google_19_0_1
        echo "Publishing 'plugin_android_push_service_firebase'"
        # npm publish plugin_android_push_service_firebase
    else \
        echo "Dry run for 'sdk'"
        # npm publish sdk --dry-run
        echo "Dry run for 'plugin_android_location_services_google'"
        # npm publish plugin_android_location_services_google --dry-run
        echo "Dry run for 'plugin_android_location_services_google_19_0_1'"
        # npm publish plugin_android_location_services_google_19_0_1 --dry-run
        echo "Dry run for 'plugin_android_push_service_firebase'"
        # npm publish plugin_android_push_service_firebase --dry-run
        echo "THIS IS DRY RUN. Check if everything is ok and then run 'npm publish'."
    fi
