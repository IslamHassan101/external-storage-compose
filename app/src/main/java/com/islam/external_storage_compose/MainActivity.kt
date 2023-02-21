package com.islam.external_storage_compose

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.islam.external_storage_compose.ui.theme.ExternalstoragecomposeTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    var message = ""
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExternalstoragecomposeTheme {
                SaveDataInStorage(context = LocalContext.current, msg = message)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SaveDataInStorage(context: Context, msg: String) {
    var massage by remember { mutableStateOf("") }
    val textMsg = remember { mutableStateOf("") }
    val activity = context as Activity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "External Storage")

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = massage,
            onValueChange = { massage = it },
            placeholder = { Text(text = "Enter your message") })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    23
                )

                val folder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                val file = File(folder, "IslamHassan.txt")
                writeTextData(file, massage, context)
                massage = ""
                Toast.makeText(context, "Data Saved..", Toast.LENGTH_SHORT).show()


            }) {
                Text(text = "Save Public")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = textMsg.value)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                        ),
                        23
                    )

                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        Uri.parse("package:${activity.packageName}")
                    )
                    context.startActivity(intent)

                    val folder =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                    val file = File(folder, "IslamHassan.txt")
                    val data = getData(file)
                    textMsg.value = data
                    Log.i("FILE", "FILE${data}, $file")
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
                Text(text = "View Public")
            }
        }
    }
}

private fun getData(myFile: File): String {
    var fileInputStream: FileInputStream? = null
    try {
        fileInputStream = FileInputStream(myFile)
        var i = -1
        val buffer = StringBuffer()
        while (fileInputStream.read().also { i = it } != -1) {
            buffer.append(i.toChar())
        }
        return buffer.toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    } finally {
        if (fileInputStream != null) {
            try {
                fileInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return ""
}


private fun writeTextData(file: File, data: String, context: Context) {
    var fileOutPutSteam: FileOutputStream? = null
    try {
        fileOutPutSteam = FileOutputStream(file)
        fileOutPutSteam.write(data.toByteArray())
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    } finally {
        if (fileOutPutSteam != null) {
            try {
                fileOutPutSteam.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
