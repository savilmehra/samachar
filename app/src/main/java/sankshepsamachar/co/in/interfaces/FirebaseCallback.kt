package sankshepsamachar.co.`in`.interfaces

import androidx.lifecycle.MutableLiveData
import sankshepsamachar.co.`in`.models.FirebaseResponseModel
import sankshepsamachar.co.`in`.models.NewsModel

interface FirebaseCallback {

   fun  onResponse(res: FirebaseResponseModel)

}