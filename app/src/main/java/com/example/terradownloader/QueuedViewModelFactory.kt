

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.terradownloader.Database.DBTeraboxDatabase

class QueuedViewModelFactory(private val context: Context, private val database: DBTeraboxDatabase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QueuedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QueuedViewModel(context, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
