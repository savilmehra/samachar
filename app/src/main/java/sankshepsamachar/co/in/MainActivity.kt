package sankshepsamachar.co.`in`

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sankshepsamachar.co.`in`.activities.ContactInformationActivity
import sankshepsamachar.co.`in`.activities.WebViewActivity
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
    private  var dataBaseNameUpload:String=""
    private var list: List<NewsModel> = ArrayList<NewsModel>()
    private  var datFormat:SimpleDateFormat?=null
    private  var currentDate:Date?=null
    private  var isOneTimeDownloaded=false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseDatabase= FirebaseDatabase.getInstance()
        MobileAds.initialize(this) {}
        currentDate = Calendar.getInstance().time
        datFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        dataBaseName = datFormat!!.format(currentDate)
        dataBaseNameUpload=datFormat!!.format(Calendar.getInstance().time)
        binding=DataBindingUtil.setContentView(this,R.layout.main_with_viewpager)
        vm=ViewModelProvider(this).get(NewsViewModel::class.java)
        adp=AdapterNews(this)
        binding.vp.adapter=adp
        binding.progressBarCyclic.visibility= View.VISIBLE

        binding.tv0.setOnClickListener {
            val intii= Intent(this, WebViewActivity::class.java)
            intii.putExtra("url","https://sites.google.com/view/sankshepsamachar/home")
            ( this ).startActivity(intii)

        }
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
        getFcm()
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

   private fun getFcm()
   {

      // binding.progressBarCyclic.visibility= View.VISIBLE
   /*    vm.getSingleItem(object :FirebaseCallback{
           override fun onResponse(res: FirebaseResponseModel) {
               binding.progressBarCyclic.visibility= View.INVISIBLE

               res.newsList?.let {

                   adp.setList(it as MutableList<NewsModel>) }
           }


       },dataBaseName,intent.extras!!["time"].toString())
*/
     /*  if (intent.extras != null && intent.extras!!["url"]!=null && intent.extras!!["title"]!=null) {
           var nModel=NewsModel()
           nModel.url=intent.extras!!["url"].toString()
           nModel.title=intent.extras!!["title"].toString()
           nModel.description=intent.extras!!["description"].toString()
           nModel.link=intent.extras!!["link"].toString()

          showFcmData(nModel)
       }
*/
   }

    fun getData()
    {
        binding.progressBarCyclic.visibility= View.VISIBLE
        vm.getDataFromRepo(object :FirebaseCallback{
            override fun onResponse(res: FirebaseResponseModel) {
                binding.progressBarCyclic.visibility= View.INVISIBLE

                if( res.newsList!!.isEmpty() && !isOneTimeDownloaded)
                {
                    currentPage+=1
                    val c = Calendar.getInstance()
                    c.time = currentDate
                    c.add(Calendar.DATE, -currentPage)
                    dataBaseName = datFormat!!.format(c.time)
                    Log.d("Daya-------",dataBaseName)
                    getData()
                }
                else
                {
                    isOneTimeDownloaded=true;
                }


                res.newsList?.let {


                    adp.setList(it as MutableList<NewsModel>) }
            }


        },dataBaseName)
    }
    private fun showFcmData(item:NewsModel)
    {
        adp.setInCurrentPosition(item)
    }
    suspend fun uploadData(n:NewsModel)
    {
        dataRef = firebaseDatabase?.getReference(dataBaseNameUpload)?.push()
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