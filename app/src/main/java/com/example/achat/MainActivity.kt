package com.example.achat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.achat.activity.NumberActivity
import com.example.achat.adapter.ViewPagerAdapter
import com.example.achat.databinding.ActivityMainBinding
import com.example.achat.ui.CallFragment
import com.example.achat.ui.ChatFragment
import com.example.achat.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth
import javax.net.ssl.SSLEngineResult.Status

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentArrList = ArrayList<Fragment>()

        fragmentArrList.add(ChatFragment())
        fragmentArrList.add(StatusFragment())
        fragmentArrList.add(CallFragment())

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null){
            startActivity(Intent(this, NumberActivity::class.java))
            finish()
        }

        val adapter = ViewPagerAdapter(this , supportFragmentManager , fragmentArrList)

        binding!!.viewPager.adapter = adapter

        binding!!.tabs.setupWithViewPager(binding!!.viewPager)

    }
}