package com.example.terradownloader


import TDDownloadModel
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.terradownloader.Adapter.TabPagerAdapter
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Repository.TeraboxRepository
import com.example.terradownloader.databinding.ActivityMainBinding
import com.example.terradownloader.interfaces.ItemClickListener
import com.example.terradownloader.utils.AndroidDownloader
import com.example.terradownloader.utils.Tdutils.displayToastless
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity(), ItemClickListener {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private lateinit var urlId: String
    private var pasteUrl: String = ""
    private lateinit var dlink: String

    private lateinit var clipboardManager: ClipboardManager;
    private lateinit var item: ClipData.Item;

    private lateinit var mDownloader: AndroidDownloader
    private var downloadTDDownloadModel: MutableList<TDDownloadModel> = ArrayList()
    private lateinit var mRepository: TeraboxRepository
    //private lateinit var mTDAdapter: TabPagerAdapter;



    private lateinit var mMainActivityMainBinding: ActivityMainBinding


    private lateinit var mTabLayout: TabLayout;
    private lateinit var mviewPager: ViewPager2;
    private lateinit var mCurerentlyDownloadingFragment: CurrentDownloadingFragment;
    private lateinit var mDownloadedFragment: DownloadedFragment;
    private lateinit var mViewTabPagerAdapter: TabPagerAdapter;

    private lateinit var mDBTeraboxDatabase: DBTeraboxDatabase


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mMainActivityMainBinding.root);
        mviewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.activity_main_tab_layout);

        //Initialize Fragments and Adapters for ViewPager

        mCurerentlyDownloadingFragment = CurrentDownloadingFragment();
        mDownloadedFragment = DownloadedFragment()

        mViewTabPagerAdapter = TabPagerAdapter(this);
        mviewPager.adapter = mViewTabPagerAdapter;
        mviewPager.offscreenPageLimit = 2;


        supportFragmentManager.beginTransaction()
            .add(R.id.activity_main_frame_layout, mCurerentlyDownloadingFragment)
            .hide(mCurerentlyDownloadingFragment)
            .add(R.id.activity_main_frame_layout, mDownloadedFragment).hide(mDownloadedFragment)
            .commitNow();

        TabLayoutMediator(
            mTabLayout, mviewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.text_currently_downloading)
                    tab.icon = getDrawable(R.drawable.baseline_download_for_offline_24)
                    mCurerentlyDownloadingFragment
                }// Customize tab titles as needed
                1 -> {
                    tab.text = getString(R.string.text_downloaded)
                    tab.icon = getDrawable(R.drawable.baseline_file_download_done_24)
                    mDownloadedFragment
                }
                // Add more tabs if necessary
            }
        }.attach();

        mRepository=(application as TerraMain).repository

        //Initlize the DB
        //To insert the data into DB that has suspend function you have to call using
//        GlobalScope.launch {
//            mDBTeraboxDatabase.currentlyDownloadingDAO().insertCurrentlyDownloadingItemToDatabase();
//        }

        //mDBTerabox.currentlyDownloadingDAO().insertCurrentlyDownloadingItemToDatabase();

        //Getting the Data from Database,


        // Initialize the views
//        textFieldEnterUrl = findViewById(R.id.textField_enter_url)
//        downloadButton = findViewById(R.id.downloadButton)
//        pasteButton = findViewById(R.id.pasteButton)
//        //mRecyclerView = findViewById(R.id.recycler_view);
//        mMyDatabaseHelper = MyDatabaseHelper(this);
//
//        tabLayout = findViewById(R.id.activity_main_tab_layout)
//        viewPager = findViewById(R.id.view_pager)
//
//        // Create an adapter that manages the recycler views for each tab
//        val adapter = TabPagerAdapter(this)
//
//        // Set the adapter for the view pager
//        viewPager.adapter = adapter

//        // Set the tabs to match the adapter
//        tabLayout.setupWithViewPager(viewPager)
//
//
//        mTDAdapter = TDAdapter(this@MainActivity, downloadTDDownloadModel, this@MainActivity)
//        mRecyclerView.setLayoutManager(LinearLayoutManager(this@MainActivity))
//        mRecyclerView.setAdapter(mTDAdapter);

    }


    override fun onStart() {
        super.onStart()
        mMainActivityMainBinding.pasteButton.setOnClickListener {
            //Toast.makeText(baseContext, "URL" + pasteUrl, Toast.LENGTH_LONG).show();
            try {

                clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                if (!clipboardManager.hasPrimaryClip()) {
                    //  d("Clip Board data", "Not Valid Data");
                    if (!clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)!!) displayToastless(
                        baseContext, "Not Valid Data"
                    );
                    textFieldEnterUrl.setText("Not Valid Data");
                } else {
                    if (clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)!!) {
                        pasteUrl = clipboardManager.primaryClip?.getItemAt(0)?.text as String;
                        textFieldEnterUrl.setText(pasteUrl);
                    }
                }
            } catch (e: Exception) {
                displayToastless(baseContext, "Can not get Copied Item");
            }
        }
//        // Additional code to be executed when the activity starts
//        downloadButton.setOnClickListener {
//            if (pasteUrl.isEmpty()) displayToastless(baseContext, "Not valid Data");
//            else {
//                handleDownloadClick(pasteUrl)
//                //displayToastLong(baseContext, "Not Valid Url");
//            }
//        }

    }

//    private fun handleDownloadClick(text: String) {
//        if (checkUrlPatterns(text)) {
//            //Log.d("Valid Url regex", "Regex match for terabox");
//            //displayToastless(baseContext,"Valid tera box Url");
//            urlId = geturlID(text);
//            //d("S param from terabox", urlId);
//
//            GlobalScope.launch {
//                val dlinkFetchResponse = TDInterface.getTDRetrofitInstance().getTdlink(urlId);
//                //d("url calld", dlink.toString());
//                dlinkFetchResponse.enqueue(object : Callback<TDPojo> {
//                    override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
//                        if (response.isSuccessful) {
//                            val responseBody = response.body()!!
//                            dlink = responseBody.dlink.toString();
//                            mFileName = responseBody.server_filename.toString();
//                            mFileSize = responseBody.size.toString();
//                            //d("File name", mFileName);
//                            startDownloadingFile(dlink);
//                            //d("Response data", responseBody.toString());
//                            //d("d link", dlink);
//                        }
//                    }
//
//                    override fun onFailure(call: Call<TDPojo>, t: Throwable) {
//                        //d("Error in retrofit call", "Retrofit call error ", t);
//                        displayToastLong(baseContext, "Could not fetch details");
//
//                    }
//                })
//                //make the api call here
//            }
//
//        } else {
//            displayToastless(baseContext, "Invalid Terabox URL");
//        }
//
//
//        //val link = textFieldEnterUrl.text.toString()
//    }



//    private fun updateDownloadStatus(downloadModel: TDDownloadModel) {
//        val index =
//            downloadTDDownloadModel.indexOfFirst { it.mDownloadId == downloadModel.mDownloadId }
//        if (index != -1) {
//            downloadTDDownloadModel[index] = downloadModel
//            runOnUiThread {
//                mTDAdapter.notifyItemChanged(index)
//            }
//        }
//    }


    override fun onCLickItem(file_path: String?) {
    }

    override fun onShareClick(downloadModel: TDDownloadModel?) {
    }

    override fun onDestroy() {
        //Add the difference of the data back to the DB to both the Tables
        super.onDestroy()
    }


//    val epochTimestamp = 1632591600L // Replace with your desired epoch timestamp
//    val formattedDate = convertEpochToDateTime(epochTimestamp)
//    println(formattedDate)

}
