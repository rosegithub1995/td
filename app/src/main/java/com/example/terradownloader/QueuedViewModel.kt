import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terradownloader.Database.DownloadingTable
import com.example.terradownloader.Repository.TeraboxRepository
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.utils.Tdutils
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date

class QueuedViewModel(val app: Application, val repository: TeraboxRepository) : ViewModel() {
    // LiveData to observe changes in the download list
    private val _downloadList = MutableLiveData<List<TDDownloadModel>>()
    val downloadList: LiveData<List<TDDownloadModel>> get() = _downloadList


    //
    private val _downloadListString = MutableLiveData<List<String>>()
    val downloadListString: MutableLiveData<List<String>> get() = _downloadListString


    fun getDownloadItems(): LiveData<List<TDDownloadModel>> {
        return _downloadList
    }

    fun getDownloadItemsString(): LiveData<List<String>> {
        return _downloadListString
    }

    fun insertDownload(mTDDownloadModel: TDDownloadModel) {
        val currentList = _downloadList.value?.toMutableList() ?: mutableListOf()

        // Add the new download to the list
        currentList.add(mTDDownloadModel)

        // Update the LiveData
        _downloadList.value = currentList
        Log.d("ViewModelUpdate", "List updated: Size = ${currentList.size}")
    }

    fun insertTODownloadString(link: String) {
        val currentList = _downloadListString.value?.toMutableList() ?: mutableListOf()

        // Add the new download to the list
        currentList.add(link)

        // Update the LiveData
        _downloadListString.value = currentList
        Log.d("ViewModelUpdate", "List updated: Size = ${currentList.size}")
    }

    fun addToDatabase(mTDDownloadModel: TDDownloadModel) = viewModelScope.launch {
        //make mTdDownloadModel to DownloadingTable
        val mDownloadingTable = DownloadingTable(
            1, 1, "teraboxurl", "teraboxurl", "teraboxurl", "teraboxurl", "teraboxurl",
            mTDDownloadModel.fileName, mTDDownloadModel.fileSize,
            "teraboxurl", "teraboxurl",
            "0", false, Date(), Date(), 1
        )
        repository.addLinkToDatabase(mDownloadingTable)
        // Add the new download to the database
        // ...
    }

    fun getDownloadListFromDatabase() = viewModelScope.launch {
        // Get the download list from the database
        // ...
    }


    suspend fun getDownloadLinkInformation(link: String) {
        try {
            if (Tdutils.internetConnection(this.app)) {
                val response = repository.fetchLinkDetails(link)
                //add the reponse to the TDdownloadModel and add to the database
            } else {
                // No internet connection
                // ...
            }
        } catch (t: Throwable) {
            // Error fetching the link information
            // ...

            when (t) {

                is IOException -> {
                    // No internet connection
                    // ...
                    Toast.makeText(this.app, "IO Error Something gone bad!!!", Toast.LENGTH_SHORT).show()
                }

                else ->{
                    Toast.makeText(this.app, "Something gone bad!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Other methods...
}
