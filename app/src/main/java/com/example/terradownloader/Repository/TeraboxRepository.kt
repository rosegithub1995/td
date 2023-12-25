package com.example.terradownloader.Repository

import android.content.Context
import android.util.Log.d
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Database.DownloadingTable
import com.example.terradownloader.interfaces.RetrofitInstance
import com.example.terradownloader.interfaces.RetrofitService
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.Tdutils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class TeraboxRepository(
    private val database: DBTeraboxDatabase
) {
    private val mRetrofitService =
        RetrofitInstance.getTDRetrofitInstance().create(RetrofitService::class.java)

    //get the details ofn the link
    // Use suspend function to perform asynchronous operation
    suspend fun fetchLinkDetails(mTeraboxFileUrl: String, mContext: Context): TDPojo? {
        return try {
            // Use suspendCoroutine to convert callback-based API to a coroutine
            val mTdPojo = kotlinx.coroutines.suspendCancellableCoroutine<TDPojo?> { continuation ->
                GlobalScope.launch {

                    mRetrofitService.getTdlink(mTeraboxFileUrl).enqueue(object : Callback<TDPojo> {
                        override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
                            if (response.isSuccessful) {
                                continuation.resume(response.body())
                            } else {
                                continuation.resume(null)
                            }
                        }

                        override fun onFailure(call: Call<TDPojo>, t: Throwable) {
                            continuation.resume(null)
                        }
                    })

                    // Handle cancellation
                    continuation.invokeOnCancellation {
                        //call.cancel()
                    }
                }
            }

            mTdPojo
        } catch (e: Exception) {
            d("Error in retrofit call", "Retrofit call error ", e)
            Tdutils.displayToastLong(mContext, "Could not fetch details")
            null
        }
    }


    //start the engine

    suspend fun startDownloadEngine(mTDDownloadModel: TDDownloadModel) {


    }
    //Add the link to the database

    suspend fun addLinkToDatabase(mDownloadingTable: DownloadingTable) =
        database.downloadedDAO().insertCurrentlyDownloadingItemToDatabase(mDownloadingTable)

    fun getAllDatabasePendingFiles() =
        database.downloadedDAO().getCurrentlyDownloadingItemFromDatabase(Tdutils.STRING_COMPLETED)

    // Get all completed Files

    fun getAllCompletedFiles() =
        database.downloadedDAO().getDownloadedItemsFromDatabase(Tdutils.STRING_COMPLETED)

    //Update the file progress using the full cclass instance

    suspend fun updateFileProgress(mDownloadingTable: DownloadingTable) =
        database.downloadedDAO().updateCurrentlyDownloadingItemInDatabase(mDownloadingTable)

    suspend fun deleteFileFromDatabase(mTeraboxFileUrl: String) =
        database.downloadedDAO().deleteByFileUrl(mTeraboxFileUrl)
}
