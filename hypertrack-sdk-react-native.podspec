require 'json'
package = JSON.parse(File.read(File.join(__dir__, './', 'package.json')))

Pod::Spec.new do |s|
  s.name              = package['name']
  s.version           = package['version']
  s.summary           = package['description']
  s.requires_arc      = true
  s.author            = "HyperTrack Inc."
  s.license           = package['license']
  s.homepage          = package['homepage']
  s.source            = { :git => 'https://github.com/hypertrack/sdk-react-native.git' }
  s.platform          = :ios, '11.0'
  s.dependency        'React-Core'
  s.dependency        'HyperTrack/Objective-C', '4.5.1'
  s.source_files      = "ios/*.{xcodeproj}", "ios/RNHyperTrack/*.{h,m}"
end
