package sankshepsamachar.co.`in`.interfaces

import androidx.lifecycle.MutableLiveData
import sankshepsamachar.co.`in`.models.NewsModel

interface FirebaseCallback {

   fun  onResponse(newsList: MutableLiveData<List<NewsModel>>)

}