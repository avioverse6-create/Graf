# Graf Messenger

A test Android messenger UI built with Kotlin, Jetpack Compose and the original [PrismalAGSL](https://github.com/styropyr0/PrismalAGSL) liquid-glass library.

## Current test scope
- Home feed with one demo channel
- Channel detail screen
- Home, Contact, Settings and Search navigation
- Light/dark appearance switch
- PrismalAGSL glass surfaces and the native Prismal liquid bottom-tab component

## Build
Open in Android Studio with JDK 17 and Android SDK 37, or run:

```bash
gradle :app:assembleDebug
```

The GitHub Actions workflow builds and uploads the debug APK on every push to `main`.

## PrismalAGSL
Graf consumes the published PrismalAGSL `v1.0.7` artifact from JitPack. The library is MIT licensed; see its [repository](https://github.com/styropyr0/PrismalAGSL) for API documentation and license terms.
