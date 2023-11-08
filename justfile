alias b := build
alias r := release

alias d := docs
alias od := open-docs

build: docs
    yarn prepare

docs:
    yarn docs

open-docs: docs
    open docs/index.html

release: docs
    npm publish --dry-run
    @echo "THIS IS DRY RUN. Check if everything is ok and then run 'npm publish'. Checklist:"
    @echo "\t- check the release steps in CONTRIBUTING"
    @echo "\t- docs are up to date"

