alias b := build

alias d := docs
alias od := open-docs

build:
    yarn prepare

docs:
    yarn docs

open-docs: docs
    open docs/index.html
