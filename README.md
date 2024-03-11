# NATIVE CODE IN FLUTTER WITH KOTLIN
This repository is a demo about using Kotlin in Flutter to perform different operations and transfer the results to Flutter.
* Reading a json file with countries and their capitals and displaying the read values as a list on the Flutter side.
  ![image](https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/9cd3d9cf-1042-481c-a2ee-fb6ec5cd72d2)
  ![Ekran görüntüsü 2024-03-09 174730](https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/2d697a67-9219-4d2f-bd1c-f6843a565ef5)
* Accessing the phone's gallery, having the user select an image and showing this image on the Flutter side.
* Accessing the phone's camera with permissions, having the user take a photo and showing this photo on the Flutter side.

<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/ef088960-a206-4a1e-aa78-e4f381d07fb0"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/a1088458-eb97-4b39-8491-8333f3394e30"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/c7098f32-9f51-4e3c-a080-5c7eeff89315"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/dcd053a9-9e2d-425f-bed4-e68ffe100090"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/16309095-2d0a-4c73-ab81-9703aeb3c7c8"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/8e1d9336-dbae-476f-aa34-1c2e68734e5c"  width="240" height ="520">
<img src="https://github.com/githuseyingur/native_code_in_flutter_with_kotlin/assets/120099096/1c99633d-4b4f-4dc3-b55d-6ae8a27f8ae0"  width="240" height ="520">

```dart
static const platform = MethodChannel('my_channel');
```
## Invoke Methods (main.dart)

```dart
String jsonString = await platform.invokeMethod('getJsonStringOfCapitals');
var base64Result = await platform.invokeMethod('selectImageInAlbum');
var base64Result = await platform.invokeMethod('takePhoto');
```
## Native (MainActivity.kt)

### Describing Request Codes For Opening Gallery & Camera
```kts
class MainActivity: FlutterActivity() {
  val CAMERA_REQUEST_CODE = 200
  val GALLERY_REQUEST_CODE = 300
```
### Features

#### 1) Reading a json file with countries and their capitals

* reading the json file
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
We use `result.success` to access the result on the Flutter side.

#### 2) Accessing the phone's gallery

* open gallery
```kts
if(call.method == "selectImageInAlbum"){
  mResult = result
  val intent = Intent(Intent.ACTION_GET_CONTENT)
  intent.type = "image/*"

  if (intent.resolveActivity(packageManager) != null) {
  startActivityForResult(intent, GALLERY_REQUEST_CODE)
  }
}
```

* onActivityResult
```kts
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
  super.onActivityResult(requestCode, resultCode, data)
  if (resultCode == RESULT_OK) {

    if (requestCode == GALLERY_REQUEST_CODE) {
      var selectedImageUri = data?.getData()
      if (selectedImageUri != null) {
        val inputStream: InputStream = contentResolver.openInputStream(selectedImageUri)!!
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        inputStream.close()
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
        mResult.success(base64Image)
      }
    }
```

#### 3) Accessing the phone's camera with permissions

* permission control
```kts
if(call.method == "takePhoto"){
  mResult = result
  if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED) {
    openCamera()
  } else {
    ActivityCompat.requestPermissions(
      this, 
      arrayOf(android.Manifest.permission.CAMERA),
      CAMERA_REQUEST_CODE
    )
  }
}
```

* the method that opens the camera
```kts
private fun openCamera() {
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  startActivityForResult(intent, CAMERA_REQUEST_CODE)
}
```

* permission result

```kts
 override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      CAMERA_REQUEST_CODE -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Kullanıcı kamera iznini verdi, kamera açma işlemine devam et
                openCamera()
            } else {
                // Kullanıcı kamera iznini reddetti, hata mesajı göster veya başka bir işlem yap
                mResult.error("PERMISSION_DENIED", "Kamera izni reddedildi.", null)
            }
        }
        // Diğer izin istek kodlarını işleyebilirsiniz (gerektiğinde)
    }
}
```

* onActivityResult
```kts
// inside of  if (resultCode == RESULT_OK) {
if (requestCode == CAMERA_REQUEST_CODE ) {
  val photo: Bitmap = data?.extras?.get("data") as Bitmap
  val base64Image: String = convertBitmapToBase64(photo)
  mResult.success(base64Image)
}
```

