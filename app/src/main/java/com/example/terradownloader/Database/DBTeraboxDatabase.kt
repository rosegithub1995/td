//package com.example.terradownloader.Database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//
//@Database(entities = [CurrentlyDownloadingTable::class, DownloadedTable::class], version = 1)
//@TypeConverters(Convertors::class)
//abstract class DBTeraboxDatabase : RoomDatabase() {
//    //Linking the Abstract DAO
//
//    abstract fun currentlyDownloadingDAO(): DownloadDAO;
//    abstract fun downloadedDAO(): DownloadDAO
//
//
//    //Create only 1 instance during the appplication launch for accessing the database
//    companion object {
//        @Volatile
//        private var mDatabaseINSTANCE: DBTeraboxDatabase? = null
//        fun getDatabaseInstance(context: Context): DBTeraboxDatabase {
//            if (mDatabaseINSTANCE == null) {
//                // Synchronized is for making it thread safety.
//                synchronized(this) {
//                    mDatabaseINSTANCE = Room.databaseBuilder(
//                        context.applicationContext,
//                        DBTeraboxDatabase::class.java,
//                        "TeraboxDB"
//                    ).build()
//                }
//            }
//            return mDatabaseINSTANCE!!
//        }
//    }
//
//}
