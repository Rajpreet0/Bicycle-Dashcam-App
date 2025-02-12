package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast


class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_download)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun downloadButton(view: View){
        downloadVideo()
    }
    fun historyButton(view: View){
        val intent: Intent = Intent(this, VideoActivity::class.java)
        startActivity(intent)
    }
    private fun downloadVideo() {
        // Replace with your Raspberry Pi’s IP and port if necessary
        val videoUrl = "http://192.168.0.175:5000/video.avi"
        val uri = Uri.parse(videoUrl)

        // Create a DownloadManager.Request with the target URI
        val request = DownloadManager.Request(uri).apply {
            // Set a title for the notification (visible in the system notification area)
            setTitle("Downloading Video")
            // Optionally set a description for the download notification
            setDescription("Downloading video.avi from Raspberry Pi.")
            // Allow the file to be scanned by MediaScanner
            allowScanningByMediaScanner()
            // Set the notification visibility after the download is complete
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // Specify the destination path; in this case, the Downloads folder
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video.avi")
        }

        // Get the system DownloadManager service
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // Enqueue the download request
        val downloadId = downloadManager.enqueue(request)

        // Optionally, you can show a toast to notify the user
        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
        }

}