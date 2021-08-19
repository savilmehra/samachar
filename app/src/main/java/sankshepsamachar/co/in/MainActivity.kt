package sankshepsamachar.co.`in`


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sankshepsamachar.co.`in`.adapter.AdapterNews
import sankshepsamachar.co.`in`.databinding.MainWithViewpagerBinding
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.FirebaseResponseModel
import sankshepsamachar.co.`in`.models.MainData
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.view_model.NewsViewModel
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainWithViewpagerBinding
    private lateinit var vm: NewsViewModel
    private  lateinit var adp:AdapterNews
    private var dataRef: DatabaseReference?=null
    private  var firebaseDatabase: FirebaseDatabase?=null
    private var currentPage:Int=0
    private  var dataBaseName:String=""
    private var list: List<NewsModel> = ArrayList<NewsModel>()
    private  var datFormat:SimpleDateFormat?=null
    private  var currentDate:Date?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseDatabase= FirebaseDatabase.getInstance()
        currentDate = Calendar.getInstance().time
        datFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dataBaseName = datFormat!!.format(currentDate)
        binding=DataBindingUtil.setContentView(this,R.layout.main_with_viewpager)
        vm=ViewModelProvider(this).get(NewsViewModel::class.java)
        adp=AdapterNews(this)
        binding.vp.adapter=adp

        binding.vp.registerOnPageChangeCallback(
            object :ViewPager2.OnPageChangeCallback()
            {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if(position==adp.itemCount-1)
                    {

                        currentPage+=1
                        val c = Calendar.getInstance()
                        c.time = currentDate
                        c.add(Calendar.DATE, -currentPage)
                        dataBaseName = datFormat!!.format(c.time)
                        Log.d("Daya-------",dataBaseName)
                        getData()

                    }



                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            }

        )
        getData()
        binding.bt.setOnClickListener {
            val ss=loadJSONFromAsset()
            val data=Gson().fromJson(ss,MainData::class.java)
            if(data!=null && data.data!!.isNotEmpty())
            {

                for( item in  data.data!!)
                {
                    val tsLong = System.nanoTime()
                    item.time=tsLong.toString()
runBlocking {

    launch { uploadData(item) }
}



                }


            }



        }



    }

    fun getData()
    {
        vm.getDataFromRepo(object :FirebaseCallback{
            override fun onResponse(res: FirebaseResponseModel) {


                res.newsList?.let { adp.setList(it as MutableList<NewsModel>) }
            }


        },dataBaseName)
    }
    suspend fun uploadData(n:NewsModel)
    {
        dataRef = firebaseDatabase?.getReference(dataBaseName)?.push()
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