# NATIVE CODE IN FLUTTER WITH KOTLIN
This repository is a demo about using Kotlin in Flutter to perform different operations and transfer the results to Flutter.
* Reading a json file with countries and their capitals and displaying the read values as a list on the Flutter side.
  ![image](https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/9cd3d9cf-1042-481c-a2ee-fb6ec5cd72d2)
  ![Ekran görüntüsü 2024-03-09 174730](https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/2d697a67-9219-4d2f-bd1c-f6843a565ef5)
* Accessing the phone's gallery, having the user select an image and showing this image on the Flutter side.
* Accessing the phone's camera with permissions, having the user take a photo and showing this photo on the Flutter side.
  
```dart
static const platform = MethodChannel('my_channel');
```
##### Invoke Methods

```dart
String jsonString = await platform.invokeMethod('getJsonStringOfCapitals');
var base64Result = await platform.invokeMethod('selectImageInAlbum');
var base64Result = await platform.invokeMethod('takePhoto');
```
##### Native
```kts
if(call.method == "getJsonStringOfCapitals") {
          lateinit var jsonString: String
          try {
            jsonString = context.assets.open("country-by-capital-city.json").bufferedReader().use { it.readText()}
            result.success(jsonString)
          } catch (ex: Exception) {
            ex.printStackTrace()
          }  
        }
```



<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/ef088960-a206-4a1e-aa78-e4f381d07fb0"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/a1088458-eb97-4b39-8491-8333f3394e30"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/c7098f32-9f51-4e3c-a080-5c7eeff89315"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/dcd053a9-9e2d-425f-bed4-e68ffe100090"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/16309095-2d0a-4c73-ab81-9703aeb3c7c8"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/8e1d9336-dbae-476f-aa34-1c2e68734e5c"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/1c99633d-4b4f-4dc3-b55d-6ae8a27f8ae0"  width="240" height ="520">
