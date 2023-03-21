# Contributing

## FAQ

### How to update the HyperTrack SDK version and make a release?

1. Update SDK version constant
   - android
     - android/gradle.properties - HyperTrackSdk_HyperTrackSDKVersion
   - ios
     - hypertrack-sdk-react-native.podspec
       - s.dependency 'HyperTrack/Objective-C', '**version**'

2. Increment wrapper version
   
   - package.json
     - version
  
3. Update CHANGELOG.md
4. Update badge in README.md
5. Do the release dry run with `just release` and verify that the release is correct (checklist is in the command output)
6. Commit and merge to master
7. Create and push a new version tag
8. Run `npm publish` to publish the package to npm
9. Create a Github repo release
   - Release title should be the current version tag

### How to change build config

#### Android

Change Android build config in `android/gradle.properties`

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

