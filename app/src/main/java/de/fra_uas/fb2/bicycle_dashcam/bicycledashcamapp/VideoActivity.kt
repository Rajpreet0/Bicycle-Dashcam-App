package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.MediaController
import android.widget.VideoView
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File



class VideoActivity : AppCompatActivity() {
    private lateinit var rvVideos: RecyclerView
    private val videoList = mutableListOf<VideoItem>()
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video)

        rvVideos = findViewById(R.id.rvVideos)
        rvVideos.layoutManager = LinearLayoutManager(this)

        // Check for storage permission if needed (for Android versions below Q)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        } else {
            loadDownloadedVideos()
        }


    }

    // Callback for the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                loadDownloadedVideos()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadDownloadedVideos() {
        // Get the public Downloads directory
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (downloadsDir != null && downloadsDir.exists()) {
            // List all files and filter for video files; here we assume .avi extension
            downloadsDir.listFiles()?.forEach { file ->
                if (file.isFile && file.name.endsWith(".avi", ignoreCase = true)) {
                    val videoItem = VideoItem(
                        fileName = file.name,
                        filePath = file.absolutePath,
                        timestamp = file.lastModified() // Last modified time in milliseconds
                    )
                    videoList.add(videoItem)
                }
            }
        }

        // Optionally, sort by timestamp (latest first)
        videoList.sortByDescending { it.timestamp }

        // Set up the adapter with the list
        val adapter = VideoAdapter(videoList)
        rvVideos.adapter =adapter
        }

}