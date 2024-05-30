package com.aarafrao.cashflowquadrant

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.aarafrao.cashflowquadrant.databinding.ActivityPdfBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class PdfActivity : AppCompatActivity(), View.OnClickListener, OnPageChangeListener {

    private lateinit var sharedPref: SharedPreferences
    lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "PdfActivity"

    private lateinit var editor: SharedPreferences.Editor
    var nightMode: Boolean = false
    //    private var bookmarksList: List<BookmarkModel>? = null

    private lateinit var nightBtn: ImageView
    private lateinit var page: ImageView
    private lateinit var btnBack: ImageView
    lateinit var txtPageNumber: TextView
    private lateinit var mainBg: ConstraintLayout
    var isTrue = true
    private lateinit var binding: ActivityPdfBinding
    lateinit var pdfView: PDFView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nightBtn = findViewById(R.id.nightMode)
        page = findViewById(R.id.page)
        btnBack = findViewById(R.id.btnBack)
        pdfView = findViewById(R.id.pdfView)
        mainBg = findViewById(R.id.mainBg)
        txtPageNumber = findViewById(R.id.txtPageNumber)

        btnBack.setOnClickListener(this)

//        inAppBilling()

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                pdfView.setNightMode(true)
                nightMode = true
                mainBg.setBackgroundColor(Color.BLACK)
                pdfView.requestLayout()
                nightBtn.setImageResource(R.drawable.ic_moonfilled)

            }

            Configuration.UI_MODE_NIGHT_NO -> {
                pdfView.setNightMode(false)
                nightMode = false
                pdfView.requestLayout()
                mainBg.setBackgroundColor(Color.WHITE)
                nightBtn.setImageResource(R.drawable.ic_moon)
            }
        }
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val pagesNo = sharedPref.getInt("page", 0)
        nightBtn.setOnClickListener(this)
        page.setOnClickListener(this)

        MobileAds.initialize(this) {}

        txtPageNumber.text = "# $pagesNo"
        //loadAd()
        pdfView.fromAsset("cashflow.pdf")
            .defaultPage(pagesNo)
            .onPageChange(this)
            .swipeHorizontal(true)
            .pageSnap(true)
            /*.scrollHandle(DefaultScrollHandle(this, false))*/
            .autoSpacing(true)
            .enableAnnotationRendering(false)
            .pageFling(true)
            .pageFitPolicy(FitPolicy.BOTH)
            .nightMode(nightMode)
            .load()
    }

//    private fun inAppBilling() {
//
//        val skuList = ArrayList<String>()
//        skuList.add("android.test.purchased")
//        val puchasesUpdatedListener = PurchasesUpdatedListener { _, _ ->
//        }
//
//        val billingClient = BillingClient.newBuilder(this)
//            .setListener(puchasesUpdatedListener).enablePendingPurchases().build()
//
//        binding.btnFab.setOnClickListener {
//            billingClient.startConnection(object : BillingClientStateListener {
//                override fun onBillingServiceDisconnected() {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onBillingSetupFinished(billingResult: BillingResult) {
//                    if (billingResult.responseCode == BillingResponseCode.OK) {
//                        val params = SkuDetailsParams.newBuilder()
//                        params.setSkusList(skuList)
//                            .setType(BillingClient.SkuType.INAPP)
//
//
//                        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
//
//                            for (skuDetails in skuDetailsList!!) {
//                                val flowPurchase = BillingFlowParams.newBuilder()
//                                    .setSkuDetails(skuDetails).build()
//
//                                val responseCode = billingClient.launchBillingFlow(this@PdfActivity, flowPurchase).responseCode
//                            }
//
//                        }
//                    }
//                }
//            })
//        }
//
//    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.nightMode ->
                if (nightMode) {
                    //loadAd()
                    // Turn off PDF night mode
                    pdfView.setNightMode(false)
                    nightMode = false
                    pdfView.requestLayout()
                    mainBg.setBackgroundColor(Color.WHITE)
                    nightBtn.setImageResource(R.drawable.ic_moon)
                    Toast.makeText(this, "Day Mode Activated", Toast.LENGTH_SHORT).show()
                } else {
                    //loadAd()
                    pdfView.setNightMode(true)
                    nightMode = true
                    mainBg.setBackgroundColor(Color.BLACK)
                    pdfView.requestLayout()
                    nightBtn.setImageResource(R.drawable.ic_moonfilled)
                    Toast.makeText(this, "Night Mode Activated", Toast.LENGTH_SHORT).show()
                }

            R.id.btnBack -> {
                onBackPressed()
                //loadAd()

            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onPageChanged(page: Int, pageCount: Int) {
        isTrue = true
        txtPageNumber.text = "# $page"
        editor = sharedPref.edit()
        editor.putInt("page", page)
        editor.apply()
        //loadAd()
        if (page % 2 == 0) {
            //loadInterstitialAdHere
            loadInterstitial()
        }

    }

    @SuppressLint("VisibleForTests")
    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3744228358425966/4983677174",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                // TODO : CHECK ALL AD UNIT IDS

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                Log.d(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad dismissed fullscreen content.")
                                mInterstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                mInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                            }
                        }
                }
            })

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
}