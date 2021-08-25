package sankshepsamachar.co.`in`.repos

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import sankshepsamachar.co.`in`.interfaces.FirebaseCallback
import sankshepsamachar.co.`in`.models.FirebaseResponseModel
import sankshepsamachar.co.`in`.models.NewsModel


class ProductsRepository(private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference,

) {
    fun getResponseFromRealtimeDatabaseUsingCallback(
        callback: FirebaseCallback,dataBaseName:String
    ) {
         val productRef: DatabaseReference = rootRef.child(dataBaseName)
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

                callback.onResponse(response) }
        }
    }
    fun getSingleItem(
        callback: FirebaseCallback,dataBaseName:String, value:String
    ) {
        val productRef: DatabaseReference = rootRef.child(dataBaseName)
        val query: Query = productRef.orderByChild("epoch").equalTo(value)
        query.get().addOnCompleteListener { task ->
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

                callback.onResponse(response) }
        }
    }
}