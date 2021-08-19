package sankshepsamachar.co.`in`


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.facebook.stetho.common.Utf8Charset
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sankshepsamachar.co.`in`.activities.UploadActivity
import sankshepsamachar.co.`in`.adapter.AdapterNews
import sankshepsamachar.co.`in`.databinding.MainWithViewpagerBinding
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.MainData
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.view_model.NewsViewModel
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainWithViewpagerBinding
    private lateinit var vm: NewsViewModel
    private  lateinit var adp:AdapterNews
    private var dataRef: DatabaseReference?=null
    private  var firebaseDatabase: FirebaseDatabase?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseDatabase= FirebaseDatabase.getInstance()

        binding=DataBindingUtil.setContentView(this,R.layout.main_with_viewpager)
        vm=ViewModelProvider(this).get(NewsViewModel::class.java)
        adp=AdapterNews(this)
        binding.vp.adapter=adp
        vm.getDataFromRepo(object :FirebaseCallback{
            override fun onResponse(newsList: MutableLiveData<List<NewsModel>>) {

                newsList.value?.let { adp.setlist(it) }
            }


        })
        binding.bt.setOnClickListener {
            val ss=loadJSONFromAsset()
            val data=Gson().fromJson(ss,MainData::class.java)
            if(data!=null && data.data!!.isNotEmpty())
            {

                for( item in  data.data!!)
                {
                    val tsLong = System.currentTimeMillis() / 1000
                    item.time=tsLong
runBlocking {

    launch { uploadData(item) }
}



                }


            }



        }



    }
    suspend fun uploadData(n:NewsModel)
    {
        dataRef = firebaseDatabase?.getReference("data")?.push()
       dataRef?.setValue(n)


    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = assets.open("data.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer,charset("UTF-8") )
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


}