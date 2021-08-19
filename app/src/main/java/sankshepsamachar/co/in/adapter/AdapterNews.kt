package sankshepsamachar.co.`in`.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import sankshepsamachar.co.`in`.MainActivity
import sankshepsamachar.co.`in`.R
import sankshepsamachar.co.`in`.activities.WebViewActivity
import sankshepsamachar.co.`in`.databinding.NewsItemBinding
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.view_model.NewsViewModel
import java.util.ArrayList


class AdapterNews(val ctx:Context) : RecyclerView.Adapter<AdapterNews.NewsHolder>() {
    private var list: MutableList<NewsModel> = ArrayList<NewsModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news: NewsModel = list[position]
        holder.title.setText(news.title.toString())
        holder.dis.setText(news.description.toString())

        holder.ivButton.setOnClickListener {
            val intii= Intent(ctx as MainActivity,WebViewActivity::class.java)
            intii.putExtra("url",news.link)
           ( ctx ).startActivity(intii)

        }

        Glide.with(ctx)
            .load(news.url)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontTransform()
            .into(holder.iv)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(d: MutableList<NewsModel>) {
        d.reverse()
        list.addAll(d)
        notifyDataSetChanged()

    }

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val dis: TextView
        val iv:ImageView
        val ivButton:ImageView


        init {
            title = itemView.findViewById(R.id.tvTitle)
            dis = itemView.findViewById(R.id.tvDetail)
            iv=itemView.findViewById(R.id.iv)
            ivButton=itemView.findViewById(R.id.ih)

        }
    }
}