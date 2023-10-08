import java.util.Date

class TDDownloadModel {
    // For displaying in Recycler View

    // For displaying in Recycler View
    enum class DownloadStatus {
        PAUSED,
        DOWNLOADING,
        COMPLETED,
        FAILED,
        STARTED
    }

    // Id from the download manager
    var idCurrentlyDownloadingFromManager: Long = -1L

    // Terabox file URL
    var teraboxFileUrl: String = ""

    // The thumbnail URL for the post
    var thumbnailUrl1: String = ""

    // The thumbnail URL for the post
    var thumbnailUrl2: String = ""

    // The thumbnail URL for the post
    var thumbnailUrl3: String = ""

    // The download link of the file it is dlink from the response
    var downloadFileUrl: String = ""

    // File name
    var fileName: String = "xyz"

    // File size
    var fileSize: String = "-1B"

    // File path
    var filePath: String = "downloads"

    // Download status
    var downloadStatus: DownloadStatus = DownloadStatus.STARTED  // Set to the desired initial status

    // Download progress
    var progress: String = "0"

    // Is download paused
    var isPaused: Boolean = false

    // Download starting date
    var downloadStartingDate: Date = Date()

    // Download finishing date (insert like Date() because Converter will convert automatically)
    var downloadFinishingDate: Date = Date()

    // File upload date (fetch from the URL response like dlink)
    var fileUploadDate: Long = 0
}
