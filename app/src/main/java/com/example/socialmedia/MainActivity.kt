package com.example.socialmedia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    // lateinit var binding: ActivityMainBinding
    val auth = FirebaseAuth.getInstance().currentUser

    lateinit var txtName: TextView
    lateinit var txtEmail: TextView
    lateinit var txtPhone: TextView
    lateinit var txtProvider: TextView
    lateinit var btnLogOut: Button
    lateinit var profile_image: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initializeView()

        if (auth != null) {
            createUI()
        } else startActivity(Intent(this, LoginActivity::class.java))

        btnLogOut.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Sucessfully Logged Out", Toast.LENGTH_SHORT).show()
            }
        }
        //generateFBKey()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun initializeView() {
        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPhone = findViewById(R.id.txtPhone)
        txtProvider = findViewById(R.id.txtProvider)
        btnLogOut = findViewById(R.id.btnLogOut)
        profile_image = findViewById(R.id.profile_image)

    }

    override fun onResume() {
        super.onResume()
        if (auth != null && intent != null) {

            createUI()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()

        }
    }

    private fun createUI() {

        val list = auth?.providerData
        var providerData: String = ""
        list?.let {
            for (provider in list) {
                providerData = providerData + " " + provider.providerId
            }
        }

        auth?.let {
            txtName.text = auth.displayName
            txtEmail.text = auth.email
            txtPhone.text = auth.phoneNumber
            txtProvider.text = providerData
            Glide
                .with(this)
                .load(auth.photoUrl)
                .fitCenter()
                .placeholder(R.drawable.profile_pic)
                .into(profile_image)


        }

//    fun generateFBKey(){
//        try {
//            val info = packageManager.getPackageInfo(
//                    "com.example.socialmedia",
//                    PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//        } catch (e: NoSuchAlgorithmException) {
//        }
//    }
    }
}