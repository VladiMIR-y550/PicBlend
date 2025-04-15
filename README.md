# CameraX Image Capture App

# PicBlend

[![CI - main](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml/badge.svg?branch=main)](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml?query=branch%3Amain)
[![CI - developer](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml/badge.svg?branch=developer)](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml?query=branch%3Adeveloper)

## Description

This project is a mobile app that captures images using CameraX, applies real-time filters, and allows users to share photos. It supports both camera capture and gallery selection features.
This project supports GitHub Actions for continuous integration.  
You can also [view the latest build for `main`](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml?query=branch%3Amain)  
and [view the latest build for `developer`](https://github.com/VladiMIR-y550/PicBlend/actions/workflows/github-ci.yml?query=branch%3Adeveloper).

## Technologies Used

- Android
- CameraX
- Jetpack Compose
- Dagger Hilt
- Kotlin
- ViewModel & StateFlow
- Unsplash API

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/VladiMIR-y550/PicBlend.git
    ```

2. Open the project in Android Studio.

3. Sync Gradle dependencies:
   - Open Android Studio and sync the project with Gradle files.

4. Set up the Unsplash API key:
   - Go to [Unsplash Developers](https://unsplash.com/developers) to get your API key and add it to `gradle.properties`:
    ```plaintext
    UNSPLASH_ACCESS_KEY="your_access_key"
    ```

5. Run the app on an emulator or device.

## Usage

Once the app is set up, you can:

- Capture photos using CameraX.
- Apply real-time filters to photos.
- Save photos to your device's gallery.
- Share photos through the app's share feature.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or issues, please contact me at [mironenko.vladimir2019@gmail.com].
