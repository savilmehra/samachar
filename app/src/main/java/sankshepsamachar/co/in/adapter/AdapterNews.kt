package sankshepsamachar.co.`in`.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import sankshepsamachar.co.`in`.MainActivity
import sankshepsamachar.co.`in`.R
import sankshepsamachar.co.`in`.activities.WebViewActivity
import sankshepsamachar.co.`in`.databinding.NewsItemBinding
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.view_model.NewsViewModel
import java.util.ArrayList


class AdapterNews(val ctx:Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: MutableList<NewsModel> = ArrayList<NewsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==0) {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false)
            return NewsHolder(itemView)
        }
        else
        {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.ads_frame, parent, false)
            return AdsHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(list.get(position).title=="Ad")
        {
            return   1
        }
        else

        {
            return   0
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news: NewsModel = list[position]

        if(holder is NewsHolder) {
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
        else
        {
            addAd(holder as AdsHolder)

        }







    }
    private fun addAd(holder:AdsHolder) {
        val builder = AdLoader.Builder(ctx, "ca-app-pub-4164184164875270/5951076620")
        builder.forNativeAd { nativeAd ->
            val adView =(ctx as MainActivity).layoutInflater
                .inflate(R.layout.ads_lyt, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            holder.frame.removeAllViews()
            holder.frame.addView(adView)
        }

        val videoOptions = VideoOptions.Builder()
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())


    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.GONE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.GONE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.GONE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.GONE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.GONE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.GONE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        val vc = nativeAd.mediaContent.videoController

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {

                    super.onVideoEnd()
                }
            }
        } else {

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(d: MutableList<NewsModel>) {
        d.reverse()
        for (position  in d.indices)
        {
            if(position % 3==0 && position!=0)
            {
                val nn=NewsModel()
                nn.title="Ad"
                d.add(position,nn)
            }
        }
        list.addAll(d)
        notifyDataSetChanged()

    }

    fun setInCurrentPosition(d: NewsModel) {
        list.clear()
    list.add(d)
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

    inner class AdsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val frame : FrameLayout
        init {
            frame=itemView.findViewById(R.id.frm)
        }
    }

}