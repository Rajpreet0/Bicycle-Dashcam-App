package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class VideoAdapter(
    private val videos: List<VideoItem>,
    private val onItemClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.tvFileName.text = video.fileName
        holder.tvTimestamp.text = dateFormatter.format(Date(video.timestamp))
        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            onItemClick(video)
        }
    }

    override fun getItemCount() = videos.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFileName: TextView = itemView.findViewById(R.id.tvFileName)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }
}