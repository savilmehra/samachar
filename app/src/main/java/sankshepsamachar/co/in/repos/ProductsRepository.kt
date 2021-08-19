package sankshepsamachar.co.`in`.repos

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.FirebaseResponseModel
import sankshepsamachar.co.`in`.models.NewsModel

class ProductsRepository(
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val productRef: DatabaseReference = rootRef.child("data")
) {
    fun getResponseFromRealtimeDatabaseUsingCallback(
        callback: FirebaseCallback
    ) {
        productRef.get().addOnCompleteListener { task ->
            val response = FirebaseResponseModel()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.newsList = result.children.map { snapShot ->
                        snapShot.getValue(NewsModel::class.java)!!
                    }
                }
            }
            response.newsList?.let {

                callback.onResponse(MutableLiveData(it)) }
        }
    }
}