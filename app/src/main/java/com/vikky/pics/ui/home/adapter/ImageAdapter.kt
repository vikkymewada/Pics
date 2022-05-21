package com.vikky.pics.ui.home.adapter


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vikky.pics.Constants
import com.vikky.pics.data.model.Photo
import javax.inject.Inject

class PhotoAdapter @Inject constructor() : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(ItemComparator) {

    var listener: PhotoClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(com.vikky.pics.R.layout.photo_item, parent, false))
    }


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo: Photo? = getItem(position)
        photo?.let {
            val maxRatio: Float = it.width!!.toFloat()/it.height!!.toFloat()
            val actualWidth = (Constants.fixHeight*maxRatio).toInt()

            //    .load("https://picsum.photos/id/"+item.id+"/"+actualWidth+"/"+actualHeight)
            Glide.with(holder.imgView.context)
                .load("https://picsum.photos/id/"+it.id+"/"+actualWidth+"/"+Constants.fixHeight)
                .thumbnail(0.25f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(ColorDrawable(Color.BLACK))
                .into(holder.imgView)
        }
    }

    inner class PhotoViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        val imgView: ImageView
        init {
            imgView = v.findViewById<View>(com.vikky.pics.R.id.image_item) as ImageView
            itemView.setOnClickListener {
                listener?.onPhotoClick(imgView, getItem(absoluteAdapterPosition) as Photo)
            }
        }
    }

    object ItemComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
    }

    interface PhotoClickListener {
        fun onPhotoClick(view: ImageView, item: Photo)
    }
}