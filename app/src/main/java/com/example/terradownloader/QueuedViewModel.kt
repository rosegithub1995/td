
import android.content.Context
import android.util.Log.d
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Repository.TeraboxRepository
import com.example.terradownloader.model.TDDownloadModel

class QueuedViewModel(val mContext: Context, val mDatabase: DBTeraboxDatabase) : ViewModel() {
    private lateinit var mRepository: TeraboxRepository

    lateinit var liveQueueDataList: MutableLiveData<List<TDDownloadModel>>
    lateinit var liveDataDownloadedList: MutableLiveData<List<TDDownloadModel>>

    init {
        liveQueueDataList = MutableLiveData()
        liveDataDownloadedList = MutableLiveData()
        mRepository = TeraboxRepository(mDatabase)
    }

    fun getQueuedListData(): MutableLiveData<List<TDDownloadModel>> {
        return liveQueueDataList
    }

    fun getDownloadListData(): MutableLiveData<List<TDDownloadModel>> {
        return liveDataDownloadedList
    }

    //function to make api call

    suspend fun makeApiCall(urlId: String, teraboxFileUrl: String) {
        //val tdPojo = mRepository.fetchLinkDetails(urlId, mContext)
        val tdPojo = null
        // create the object and add the details to the list
        var mItem: TDDownloadModel? = null
        if (tdPojo != null) {
            mItem = TDDownloadModel()
//            mItem.teraboxFileUrl = teraboxFileUrl
//            mItem.icon = tdPojo.thumbs.icon
//            mItem.thumbnailUrl1 = tdPojo.thumbs.url1
//            mItem.thumbnailUrl2 = tdPojo.thumbs.url2
//            mItem.thumbnailUrl3 = tdPojo.thumbs.url3
//            mItem.downloadFileUrl = tdPojo.dlink
//            mItem.fileName = tdPojo.server_filename
//            mItem.fileSize = tdPojo.size
//            mItem.filePath = "/downloads"
//            mItem.downloadStatus = Tdutils.STRING_FETCHING
//            mItem.progress = "0"
//            mItem.isPaused = true
//            mItem.downloadStartingDate = Date()
//            mItem.downloadFinishingDate = Date()
//            mItem.fileUploadDate = tdPojo.server_mtime.toLong()
//            mItem.category = tdPojo.category
            //add mItem to the list
        } else {
            mItem = TDDownloadModel()
            mItem.fileName = teraboxFileUrl
            //
        }
        updateLiveDataWithItem(mItem)
    }

    private fun updateLiveDataWithItem(item: TDDownloadModel) {
        val currentList = liveQueueDataList.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        liveQueueDataList.postValue(currentList)
        d("DownloadList", "Size: ${liveQueueDataList.value?.size}")
    }
}
