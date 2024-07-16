package com.example.newsfeed

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(), NewsItemClicked {

    lateinit var recyclerView: RecyclerView
    private lateinit var adapter : NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        adapter = NewsListAdapter( this)
        recyclerView.adapter = adapter
    }

    private fun fetchData() {
//        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=47a5daeba16f43798a2e3401f03b3caf"

        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=47a5daeba16f43798a2e3401f03b3caf"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null, Response.Listener {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(newsJsonObject.getString("title"),
                                    newsJsonObject.getString("author"),
                                    newsJsonObject.getString("url"),
                                    newsJsonObject.getString("urlToImage")
                    )

//                    saveToInternalStorage( BitmapFactory.decodeStream( FileInputStream(f))i)
//                    saveToInternalStorage(i)
//                    saveToInternalStorage(BitmapFactory.decodeFile(.absolutePath))

                    newsArray.add(news)
                }

                adapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Log.d("Error", "Error Listener")
            }
            )   {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

//    private fun saveToInternalStorage( i : Int): String? {
//        val newsViewHolder = NewsViewHolder(itemView ).image
//        val bitmapImage = .getDrawable().getBitmap()
//        val cw = ContextWrapper(applicationContext)
//        // path to /data/data/yourapp/app_data/imageDir
//        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
//        // Create imageDir
//        val mypath = File(directory, "Image$i.jpg")
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(mypath)
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                fos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return directory.getAbsolutePath()
//    }

    // Load image from internal storage

//    private fun loadImageFromStorage(path: String) {
//        try {
//            val f = File(path, "profile.jpg")
//            val b = BitmapFactory.decodeStream(FileInputStream(f))
//            val img: ImageView = findViewById<View>(android.R.id.imgPicker) as ImageView
//            img.setImageBitmap(b)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//    }

//    override fun onItemClicked(item: String) {
//        Toast.makeText(this, "Clicked item is $item", Toast.LENGTH_LONG ).show()
//    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
        Toast.makeText(this, "Author is ${item.author}", Toast.LENGTH_LONG ).show()
    }

}


//class ImageStorageManager {
//    companion object {
//        fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): String {
//            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
//                bitmapImage.compress(Bitmap.CompressFormat.PNG, 25, fos)
//            }
//            return context.filesDir.absolutePath
//        }
//
//        fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
//            val directory = context.filesDir
//            val file = File(directory, imageFileName)
//            return BitmapFactory.decodeStream(FileInputStream(file))
//        }
//
//        fun deleteImageFromInternalStorage(context: Context, imageFileName: String): Boolean {
//            val dir = context.filesDir
//            val file = File(dir, imageFileName)
//            return file.delete()
//        }
//    }
//}