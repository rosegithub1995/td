import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.utils.Tdutils

class MyDatabaseHelper(mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "terabox_database.db"
        private var DATABASE_VERSION = 1
        private const val TABLE_NAME = "TERABOX_TABLE_INFORMATION"
        private const val COLUMN_ID = "_ID";
        private const val COLUMN_DOWNLOAD_ID_OF_MANAGER = "DOWNLOAD_ID_MANAGER";
        private const val COLUMN_FILE_NAME = "FILE_NAME";
        private const val COLUMN_FILE_SIZE = "FILE_SIZE";
        private const val COLUMN_FILE_PATH = "FILE_PATH";
        private const val COLUMN_STATUS = "STATUS"
        private const val COLUMN_PROGRESS = "PROGRESS"
        private const val COLUMN_IS_PAUSED = "IS_PAUSED"
        private const val COLUMN_DOWNLOAD_URL =
            "DOWNLOAD_URL"// like dlink="https://d4.terabox.com/bunjiijn"
        private const val COLUMN_TERABOX_FILE_URL =
            "TERABOX_FILE_URL" //LIKE  https://terabo.com/s/sbdvij

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create your database tables here
        // Example: db?.execSQL("CREATE TABLE IF NOT EXISTS your_table_name (column1_type column1_name, column2_type column2_name, ...);")

        // Create your database table here
        // Example: db?.execSQL("CREATE TABLE IF NOT EXISTS your_table_name (column1_type column1_name, column2_type column2_name, ...);")

        val sqlCreateTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," + "$COLUMN_DOWNLOAD_ID_OF_MANAGER INTEGER," + "$COLUMN_FILE_NAME TEXT," + "$COLUMN_FILE_SIZE TEXT," + "$COLUMN_FILE_PATH TEXT," + "$COLUMN_STATUS TEXT," + "$COLUMN_PROGRESS TEXT," + "$COLUMN_IS_PAUSED INTEGER," + "$COLUMN_DOWNLOAD_URL TEXT," + "$COLUMN_TERABOX_FILE_URL TEXT" + ");"

        db?.execSQL(sqlCreateTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades here
        // Example: db?.execSQL("DROP TABLE IF EXISTS your_table_name;")
        // Then, recreate the table with a new schema
        // Example: onCreate(db)

        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        }
        DATABASE_VERSION = DATABASE_VERSION + 1
        onCreate(db);
    }

    fun addToTeraboxDatabase(context: Context, mTDDownloadModel: TDDownloadModel) {
        // Check if the downloadId already exists in the database
        val existingDownloadId = getDownloadIdIfExists(mTDDownloadModel.mTeraboxFileUrl)

        if (existingDownloadId != -1L) {
            // The downloadId already exists, handle this case (e.g., update or show a message)
            Tdutils.displayToastless(context, "DownloadId already exists in the database")
        } else {
            // The downloadId doesn't exist, insert the new record into the database
            val db: SQLiteDatabase = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COLUMN_DOWNLOAD_ID_OF_MANAGER, mTDDownloadModel.mDownloadId)
            contentValues.put(COLUMN_FILE_NAME, mTDDownloadModel.mFileName)
            contentValues.put(COLUMN_FILE_SIZE, mTDDownloadModel.mFileSize)
            contentValues.put(COLUMN_FILE_PATH, mTDDownloadModel.mFilePath)
            contentValues.put(COLUMN_STATUS, mTDDownloadModel.mStatus)
            contentValues.put(COLUMN_PROGRESS, mTDDownloadModel.mProgress)
            contentValues.put(COLUMN_IS_PAUSED, mTDDownloadModel.mIsPaused)
            contentValues.put(COLUMN_DOWNLOAD_URL, mTDDownloadModel.mDownloadUrl)
            contentValues.put(COLUMN_TERABOX_FILE_URL, mTDDownloadModel.mTeraboxFileUrl)

            val result = db.insert(TABLE_NAME, null, contentValues)
            if (result == (-1).toLong()) {
                Tdutils.displayToastless(context, "Could not insert into Database")
            }
        }
    }

    @SuppressLint("Range")
    private fun getDownloadIdIfExists(downloadId: String): Long {
        val db = this.readableDatabase
        val query =
            "SELECT $COLUMN_DOWNLOAD_ID_OF_MANAGER FROM $TABLE_NAME WHERE $COLUMN_TERABOX_FILE_URL = ?"
        val cursor = db.rawQuery(query, arrayOf(downloadId.toString()))

        return if (cursor != null && cursor.moveToFirst()) {
            val existingDownloadId =
                cursor.getLong(cursor.getColumnIndex(COLUMN_DOWNLOAD_ID_OF_MANAGER))
            cursor.close()
            existingDownloadId
        } else {
            -1L
        }
    }


    fun updateToTeraboxDatabase(
        context: Context, mTDDownloadModel: TDDownloadModel
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_STATUS, mTDDownloadModel.mStatus)
        contentValues.put(COLUMN_PROGRESS, mTDDownloadModel.mProgress)
        contentValues.put(COLUMN_IS_PAUSED, mTDDownloadModel.mIsPaused) // Convert boolean to int
        contentValues.put(COLUMN_DOWNLOAD_URL, mTDDownloadModel.mDownloadUrl)
        contentValues.put(COLUMN_TERABOX_FILE_URL, mTDDownloadModel.mTeraboxFileUrl)

        val whereClause = "$COLUMN_DOWNLOAD_ID_OF_MANAGER = ?"
        val whereArgs = arrayOf(mTDDownloadModel.mDownloadId.toString())

        val result = db.update(TABLE_NAME, contentValues, whereClause, whereArgs)

        if (result == 0) {
            // No rows were updated, you can handle this case if needed
            Tdutils.displayToastless(context, "No rows were updated")
        } else if (result == -1) {
            // An error occurred during the update, you can handle this case if needed
            Tdutils.displayToastless(context, "Error updating the database")
        } else {
            // Rows were updated successfully
            Tdutils.displayToastless(context, "Database updated successfully")
        }
    }


}
