# Facts

This project is created to display facts from JSON URL feed in Android.

## SDK Requirements
- Target SDK version	: 29
- Compile SDK version	: 29
- Minimum SDK version	: 25
- Kotlin version      : 1.3.71
- Gradle version		  : 3.6.1

## External Components
- Retrofit : HTTP library to make network calls.
- Glide : Image downloading and caching library for Android.
- Moshi : Serialization/deserialization library that can convert into JSON and back.
- Mokito - unit test framework to test-drive the development of your Android app.

## Usage
- App loads Facts list screen, application fetches the facts information from configured REST URL with progress indicator and loading message on screen.
- User can scroll throgh all the facts with title, description and image information on invididual list items.
- Image loading process will be lazy loading around list items. Image will also be cached in the app.
