package sankshepsamachar.co.`in`.models

import androidx.annotation.NonNull
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 class NewsModel {

     @SerializedName("link")
     @Expose
     @NonNull
     var link: String? = null
    @SerializedName("url")
    @Expose
    @NonNull
    var url: String? = null
     @SerializedName("title")
     @Expose
     @NonNull
     var title: String? = null
     @SerializedName("description")
     @Expose
     @NonNull
     var description: String? = null
     @SerializedName("time")
     @Expose
     @NonNull
     var time:String?=null

}