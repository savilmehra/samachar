package sankshepsamachar.co.`in`.repos

import android.util.Log
import com.google.firebase.database.*
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
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                Log.d("TAG-------------------",  dataSnapshot.key.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG++++++", "loadPost:onCancelled", databaseError.toException())
            }
        }
        productRef.orderByChild("time").equalTo(value).addListenerForSingleValueEvent(postListener)
    }
}