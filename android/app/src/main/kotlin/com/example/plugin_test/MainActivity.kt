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
import android.widget.Toast


class MainActivity: FlutterActivity() {
  lateinit var mResult:MethodChannel.Result
  override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "random_channel").setMethodCallHandler {
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
              startActivityForResult(intent, 1)
            }
        }
        else if(call.method == "takePhoto"){

          mResult = result
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 0)
            }
   /*       if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, 0)
    } else {
        // Kamera uygulaması bulunamadı, hata mesajını göster
        // veya uygun bir işlem yap
        Toast.makeText(this, "Kamera uygulaması bulunamadı", Toast.LENGTH_SHORT).show()
    }
    } else {
         //Kamera izni henüz alınmamış, izin isteği başlat
      ActivityCompat.requestPermissions(
           this, 
           arrayOf(android.Manifest.permission.CAMERA),
           0
      )
    }*/
          
        }
        else {
          result.notImplemented()
        }
    }

  }
  /*private fun openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, 0)
    } else {
        // Kamera uygulaması bulunamadı, hata mesajını göster
        // veya uygun bir işlem yap
        Toast.makeText(this, "Kamera uygulaması bulunamadı", Toast.LENGTH_SHORT).show()
    }
  }*/
  /*override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
        0 -> {
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
}*/
 
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
 
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
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
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
         
        }
    }
  
 
}

