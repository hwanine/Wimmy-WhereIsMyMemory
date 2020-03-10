package com.example.wimmy.Adapter

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wimmy.R
import com.example.wimmy.ThumbnailAsyncTask
import com.example.wimmy.db.PhotoData
import com.example.wimmy.db.thumbnailData

class RecyclerAdapterPhoto(val context: Activity?, var list: ArrayList<PhotoData>, val itemClick: (PhotoData, Int, ImageView) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterPhoto.Holder>()
{
    private var size : Int = 200
    private var padding_size = 200
    private var bitmapList = MutableList<Bitmap?>(list.size) { _ -> null }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        //thumbnail_imgview 변수 받아오기
        var thumbnail: ImageView = itemView!!.findViewById<ImageView>(R.id.thumbnail_img)
        var text = itemView?.findViewById<TextView>(R.id.thumbnail_img_text)

        fun bind(data : PhotoData, num: Int) {
            //photo_view의 내부 값 설정
            val layoutParam = thumbnail.layoutParams as ViewGroup.MarginLayoutParams
            thumbnail.layoutParams.width = size
            thumbnail.layoutParams.height = size
            layoutParam.setMargins(padding_size, padding_size, padding_size, padding_size)

            if(bitmapList[adapterPosition] == null) {
                thumbnail.setImageResource(0)
                ThumbnailAsyncTask( this, thumbnail, data.photo_id, bitmapList).execute(context!!.applicationContext)
            }
            else thumbnail.setImageBitmap(bitmapList[adapterPosition])

            text!!.text = data.name
            itemView.setOnClickListener { itemClick(data, num, thumbnail) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.thumbnail_imgview, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setPhotoSize(size : Int, padding_size : Int) {
        this.size = size
        this.padding_size = padding_size
        notifyDataSetChanged()
    }

    fun setThumbnailList(list : ArrayList<PhotoData>?) {
        if(list.isNullOrEmpty()) this.list = ArrayList<PhotoData>()
        else {
            this.list = list
            bitmapList = MutableList<Bitmap?>(list.size) { _ -> null}
            notifyDataSetChanged()
        }
    }

    fun getThumbnailList() : ArrayList<PhotoData> {
        return list
    }

    fun addThumbnailList(photoData : PhotoData) {
        list.add(photoData)
        bitmapList.add(null)
        notifyDataSetChanged()
    }

    fun getSize() : Int {
        return list.size
    }
}
