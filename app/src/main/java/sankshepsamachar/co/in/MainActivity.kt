package sankshepsamachar.co.`in`


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.FirebaseDatabase
import sankshepsamachar.co.`in`.activities.UploadActivity
import sankshepsamachar.co.`in`.adapter.AdapterNews
import sankshepsamachar.co.`in`.databinding.MainWithViewpagerBinding
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.NewsModel
import sankshepsamachar.co.`in`.view_model.NewsViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainWithViewpagerBinding
    private lateinit var vm: NewsViewModel
    private  lateinit var adp:AdapterNews


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




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

            val intt=Intent(this, UploadActivity::class.java)
            startActivity(intt)



        }



    }


}