package com.example.terradownloader.Repository

import android.content.Context
import android.util.Log.d
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Database.DownloadingTable
import com.example.terradownloader.interfaces.RetrofitInstance
import com.example.terradownloader.interfaces.RetrofitService
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.Tdutils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeraboxRepository(
    private val database: DBTeraboxDatabase
) {
    private val mRetrofitService =
        RetrofitInstance.getTDRetrofitInstance().create(RetrofitService::class.java)

    //get the details ofn the link
    suspend fun fetchLinkDetails(mTeraboxFileUrl: String, mContext: Context): TDPojo {
        //fetch information from the link
        val mResponse = mRetrofitService.getTdlink(mTeraboxFileUrl)
        var mTdPojo: TDPojo? = null
        GlobalScope.launch {
            mResponse.enqueue(object : Callback<TDPojo> {
                override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
                    if (response.isSuccessful) {
                        mTdPojo = response.body()!!
                        d("Error in retrofit call", mTdPojo.toString());
                    }
                }

                override fun onFailure(call: Call<TDPojo>, t: Throwable) {
                    d("Error in retrofit call", "Retrofit call error ", t);
                    Tdutils.displayToastLong(mContext, "Could not fetch details");
                }
            })
        }
        return mTdPojo!!
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
