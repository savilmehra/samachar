package sankshepsamachar.co.`in`.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import sankshepsamachar.co.`in`.R
import sankshepsamachar.co.`in`.databinding.ContactInfoBinding

class ContactInformationActivity:AppCompatActivity() {


    lateinit var  binding:ContactInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.contact_info)
    }


}