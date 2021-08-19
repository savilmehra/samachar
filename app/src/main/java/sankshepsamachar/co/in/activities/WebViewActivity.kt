package sankshepsamachar.co.`in`.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import sankshepsamachar.co.`in`.R
import sankshepsamachar.co.`in`.databinding.WebviewActivityBinding

class WebViewActivity:AppCompatActivity() {

    private lateinit var binding:WebviewActivityBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding=DataBindingUtil.setContentView(this, R.layout.webview_activity)

        intent.getStringExtra("url")?.let { binding.webView.loadUrl(it) }


    }
}