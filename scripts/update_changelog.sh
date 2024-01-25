#!/bin/bash
set -euo pipefail

while [ "$#" -gt 0 ]; do
    case "$1" in
    -w)
        wrapper_version="$2"
        shift 2
        ;;
    -i)
        ios_version="$2"
        shift 2
        ;;
    -a)
        android_version="$2"
        shift 2
        ;;
    *)
        echo "Usage: $0 [-w wrapper_version] [-i ios_version] [-a android_version]"
        exit 1
        ;;
    esac
done

changelog_file="CHANGELOG.md"

if [ ! -f "$changelog_file" ]; then
    echo "Error: $changelog_file not found."
    exit 1
fi

if [ -z "$wrapper_version" ]; then
    echo "Error: wrapper_version is required."
    exit 1
fi

date=$(date +%Y-%m-%d)

$(echo "[$wrapper_version]: https://github.com/hypertrack/sdk-react-native/releases/tag/$wrapper_version" >>$changelog_file)

sed -i '' -e "8 i\\
" $changelog_file
sed -i '' -e "8 i\\
## [$wrapper_version] - $date" $changelog_file
sed -i '' -e "9 i\\
" $changelog_file
sed -i '' -e "10 i\\
### Changed" $changelog_file
sed -i '' -e "11 i\\
" $changelog_file

if [ -n "$android_version" ]; then
    sed -i '' -e "12 i\\
- Updated HyperTrack SDK Android to $android_version" $changelog_file
fi

if [ -n "$ios_version" ]; then
    sed -i '' -e "12 i\\
- Updated HyperTrack SDK iOS to $ios_version" $changelog_file
fi
