package com.example.newsfeed

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class NewsListAdapter( private val listener : NewsItemClicked) : RecyclerView.Adapter<NewsViewHolder>() {

    private val items : ArrayList<News> = ArrayList()
//    lateinit var bitmap : Bitmap

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
//        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.author.text = currentItem.author
//        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)
//        val width = currentItem.imageUrl

        // Simple Target.
//        Glide.with(holder.itemView.context)
//            .asBitmap()
//            .load(currentItem.imageUrl)
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
//                    val w = bitmap.width
//                    val h = bitmap.height
//                    Log.i("W&H", "Width is $w and height is $h")
//                    holder.image.setImageBitmap(bitmap)
//                }
//
//            })

        // Custom Target
        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(currentItem.imageUrl)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val w = resource.width
                    val h = resource.height
                    Log.i("W&H", "Width is $w and height is $h")
                    holder.image.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })


        // Request Listener.
//        Glide.with(holder.itemView.context)
//            .asBitmap()
//            .load(currentItem.imageUrl)
//            .listener(object : RequestListener<Bitmap> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    Toast.makeText(holder.itemView.context, "Unexpected error occurred", Toast.LENGTH_LONG).show()
//                    return false;
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    val w = resource?.width
//                    val h = resource?.height
//                    Log.i("W&H", "Width is $w and height is $h")
//                    holder.image.setImageBitmap(resource)
//                    return false;
//
//                }
//
//            })
//        val height = holder.image.height
//        Log.i("W&H", "Width is $width and height is $height")
//

//        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into( CustomTarget<Bitmap>() {
//            @Override
//             fun onResourceReady(@NonNull resource : Bitmap, @Nullable transition : Transition<in Bitmap?>) {
//                val width = resource.getWidth();
//                val height = resource.getHeight()
//                Log.i("W&H", "Width is $width and height is $height")
//            }
//
//            @Override
//            fun onLoadCleared(@Nullable  placeholder : Drawable) {
//            }
//        })

//
        saveToInternalStorage(position, holder, currentItem.imageUrl)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews : ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

    private fun saveToInternalStorage(i: Int, holder: NewsViewHolder, imageUrl: String) {

//        try{

        if(Build.VERSION.SDK_INT < 28) {
            try {

                var newsViewHolderDrawable = holder.image.drawable
//                val bitmapImage : Bitmap = newsViewHolderDrawable.getBit
                val bitmapImage = (holder.image.getDrawable().getCurrent() as BitmapDrawable).bitmap
                val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
//        val cw = ContextWrapper(applicationContext)
                // path to /data/data/yourapp/app_data/imageDir
//        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
                // Create imageDir
                val mypath = File(file, "Image$i.jpg")
//                mypath.delete()
                if (!mypath.exists()) {
                    mypath.createNewFile()
                }
                var fos: FileOutputStream? = null
//        var newsObj = News()
//        val istream : InputStream = imageUrl.body()!!.byteStream()
                try {
                    fos = FileOutputStream(mypath)
//            fos.write()
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
//            Toast.makeText(MainActivity., )
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
//        return directory.getAbsolutePath()

            }catch(e : Exception) {
                e.printStackTrace()

            }

        }

                }



}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleView:TextView = itemView.findViewById(R.id.title)
    val author : TextView = itemView.findViewById(R.id.author)
    val image : ImageView = itemView.findViewById(R.id.imageViewNews)
}

interface NewsItemClicked{
    fun onItemClicked(item : News)
}