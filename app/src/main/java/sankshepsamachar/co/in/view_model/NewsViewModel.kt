package sankshepsamachar.co.`in`.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.repos.ProductsRepository

class NewsViewModel():ViewModel() {



    fun getDataFromRepo(ff:FirebaseCallback,time:String)
    {
        ProductsRepository().getResponseFromRealtimeDatabaseUsingCallback(
      ff,time

        )

    }

    fun getSingleItem(ff:FirebaseCallback,node:String,epoch:String)
    {
        ProductsRepository().getSingleItem(
            ff,node,epoch

        )

    }



}