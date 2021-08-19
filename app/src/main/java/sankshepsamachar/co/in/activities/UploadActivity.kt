package sankshepsamachar.co.`in`.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sankshepsamachar.co.`in`.R
import sankshepsamachar.co.`in`.databinding.UploadFileBinding
import sankshepsamachar.co.`in`.models.NewsModel

import kotlin.coroutines.CoroutineContext


private const val LOCATION_PERMISSION_REQUEST_CODE = 999
private const val TAG = "MainActivity"
class UploadActivity : AppCompatActivity() , CoroutineScope {
    private var job: Job = Job()



    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            uploaddata(data?.data.toString())
            Log.d("data found url---------",data?.data.toString())
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                openSomeActivityForResult2(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                openSomeActivityForResult2(intent)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                99
            )
        }
    }
    fun openSomeActivityForResult2(intent:Intent) {

        resultLauncher.launch(intent)
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var url:String?=null
    private lateinit var binding: UploadFileBinding

    private var dataRef:DatabaseReference?=null
    private  var firebaseDatabase:FirebaseDatabase?=null

    private  var pr:ProgressBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!checkPermission())
            requestPermission()
        firebaseDatabase= FirebaseDatabase.getInstance()
        dataRef = firebaseDatabase?.getReference("data")?.push()


        binding = DataBindingUtil.setContentView(this, R.layout.upload_file)
        pr = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(300, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        binding.rly.addView(pr, params)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        pr?.visibility= View.INVISIBLE
        binding.uplod.setOnClickListener {
            val tsLong = System.currentTimeMillis() / 1000
            pr?.visibility= View.VISIBLE
            val newsModel=   NewsModel()
            newsModel.url=url
            newsModel.link=binding.link.text.toString()
            newsModel.title=binding.title.text.toString()
            newsModel.description=binding.des.text.toString()
            newsModel.time=tsLong
            val initTask =  dataRef?.setValue(newsModel)

            initTask?.addOnSuccessListener(OnSuccessListener<Any?> {
                pr?.visibility= View.INVISIBLE
                finish()
            })

            initTask?.addOnFailureListener(OnFailureListener {
                pr?.visibility= View.INVISIBLE
                finish()
            })
        }

        binding.filePicker.setOnClickListener {
            openSomeActivityForResult()

        }


    }
    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("permissiion","An")
            return Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val result1 = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            Log.d("permissiion","f")
            return  result == PackageManager.PERMISSION_GRANTED && result1 ==
                    PackageManager.PERMISSION_GRANTED
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        else -> {

            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun openSomeActivityForResult() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        resultLauncher.launch(intent)
    }
    private fun uploaddata( uri:String) {

        val storageRef = FirebaseStorage.getInstance().reference.child("files")
        pr?.visibility= View.VISIBLE

        val storageRefrence =
            storageRef!!.child("assignments" + Uri.parse(uri).lastPathSegment)

        val uploadTask = storageRefrence.putFile(Uri.parse(uri))
        Log.d("url_list_size===", uri.toString())
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRefrence.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                binding.fileurl.text=downloadUri.toString()

                url=downloadUri.toString()
                pr?.visibility= View.INVISIBLE
                Log.d("downloaded file ------", downloadUri.toString())
            } else {

            }
        }



    }
    override fun onStart() {
        super.onStart() }
    override fun onPause() {
        super.onPause()
    }
    override fun onResume() {
        super.onResume()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

        }
    }

}