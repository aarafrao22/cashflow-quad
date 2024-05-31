package com.aarafrao.cashflowquadrant

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.aarafrao.cashflowquadrant.databinding.ActivityFirstBinding
import com.google.android.gms.ads.AdRequest

class FirstActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFirstBinding

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)


        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        val linearLayout1: LinearLayout = findViewById(R.id.linearLayout2)
//        val p_k: LinearLayout = findViewById(R.id.p_k)
//        val yaram: LinearLayout = findViewById(R.id.yaram)
//        val mushaf: LinearLayout = findViewById(R.id.mushaf)

        linearLayout.setOnClickListener(this)
        linearLayout1.setOnClickListener(this)


        binding.lop.setOnClickListener(this)
        binding.pom.setOnClickListener(this)
//        p_k.setOnClickListener(this)
//        yaram.setOnClickListener(this)
//        mushaf.setOnClickListener(this)

        AdRequest.Builder().build()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.linearLayout -> {
                val intent = Intent(this, PdfActivity::class.java)

                startActivity(intent)

            }

            R.id.linearLayout2 -> {
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("myPref", MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                val intent = Intent(this, PdfActivity::class.java)
                startActivity(intent)

            }

            R.id.lop -> {
                shiftToMore("https://play.google.com/store/apps/details?id=com.aarafrao.phsycologyofmoney")
            }

            R.id.pom -> {
                shiftToMore("https://play.google.com/store/apps/details?id=com.aarafrao.a48lawsofpower")

            }
        }
    }


    private fun shiftToMore(url: String) {

        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse(url)
            )
        )
    }
}