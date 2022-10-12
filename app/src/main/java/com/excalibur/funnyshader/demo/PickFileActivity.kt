package com.excalibur.funnyshader.demo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.excalibur.funnyshader.R

class PickFileActivity : AppCompatActivity() {

    private val resultPickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        Log.e("HVV1312","uri: $uri")
        val inputStream = contentResolver.openInputStream(uri!!)
        Log.e("HVV1312","inputStream: $inputStream")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_file)

        findViewById<Button>(R.id.btn_pick_file).setOnClickListener {
            pickFile()
        }
    }

    private fun pickFile() {
        resultPickFile.launch("*/*")
    }




}