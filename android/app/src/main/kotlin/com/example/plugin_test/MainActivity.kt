package com.example.plugin_test

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import java.io.InputStream
import android.content.Intent
import android.util.Base64
import android.provider.MediaStore
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.util.Log


class MainActivity: FlutterActivity() {
  val CAMERA_REQUEST_CODE = 200
  val GALLERY_REQUEST_CODE = 300
  
  lateinit var mResult:MethodChannel.Result
  override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "my_channel").setMethodCallHandler {
      call, result ->
        if(call.method == "getJsonStringOfCapitals") {
          lateinit var jsonString: String
          try {
            jsonString = context.assets.open("country-by-capital-city.json").bufferedReader().use { it.readText()}
            result.success(jsonString)
          } catch (ex: Exception) {
            ex.printStackTrace()
          }  
        }else if(call.method == "selectImageInAlbum"){
          mResult = result
          val intent = Intent(Intent.ACTION_GET_CONTENT)
          intent.type = "image/*"
            if (intent.resolveActivity(packageManager) != null) {
              startActivityForResult(intent, GALLERY_REQUEST_CODE)
            }
        }
        else if(call.method == "takePhoto"){
          mResult = result
          if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
          } else {
         //Kamera izni henüz alınmamış, izin isteği başlat
            ActivityCompat.requestPermissions(
            this, 
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
            )
          }
        }
        else {
          result.notImplemented()
        }
    }

  }
  private fun openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(intent, CAMERA_REQUEST_CODE)
  }
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

          if (requestCode == CAMERA_REQUEST_CODE ) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val base64Image: String = convertBitmapToBase64(photo)
            mResult.success(base64Image)
          }

        }
  }

  private fun convertBitmapToBase64(bitmap: Bitmap): String {
      val byteArrayOutputStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
      val byteArray = byteArrayOutputStream.toByteArray()
      return Base64.encodeToString(byteArray, Base64.DEFAULT)
  }
 
}

