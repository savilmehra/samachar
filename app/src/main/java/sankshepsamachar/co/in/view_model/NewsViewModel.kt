package sankshepsamachar.co.`in`.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.repos.ProductsRepository

class NewsViewModel():ViewModel() {

    var newsList:LiveData<List<NewsModel>>?=null


    fun getDataFromRepo(ff:FirebaseCallback)
    {
        ProductsRepository().getResponseFromRealtimeDatabaseUsingCallback(
      ff

        )

    }





}