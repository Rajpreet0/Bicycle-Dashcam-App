package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.MediaController
import android.widget.VideoView
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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

        // For API levels below Q, check if we need storage permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        } else {
            loadDownloadedVideos()
        }
    }

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
            // Filter for AVI files; adjust extension if needed
            downloadsDir.listFiles()?.forEach { file ->
                if (file.isFile && file.name.endsWith(".avi", ignoreCase = true)) {
                    videoList.add(
                        VideoItem(
                            fileName = file.name,
                            filePath = file.absolutePath,
                            timestamp = file.lastModified()
                        )
                    )
                }
            }
        }

        // Optionally, sort the list (latest first)
        videoList.sortByDescending { it.timestamp }

        // Set up the adapter with a click listener lambda
        val adapter = VideoAdapter(videoList) { videoItem ->
            playVideo(videoItem)
        }
        rvVideos.adapter = adapter
    }

    private fun playVideo(videoItem: VideoItem) {
        val file = File(videoItem.filePath)
        // Get a content URI using FileProvider for API 24+
        val fileUri: Uri =
            FileProvider.getUriForFile(this, "de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.provider", file)

        // Create an intent to view the video
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "video/*")
            // Grant temporary read permission to the external app
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Launch the intent. The user will be prompted to choose a media player if more than one is available.
        startActivity(intent)
    }

    fun historyButton(view: View){
        val intent: Intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
    fun settingsButton(view: View){
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
    fun dashboardButton(view: View) {
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
    }