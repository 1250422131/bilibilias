package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.ActivityUserInfoBinding
import com.imcys.bilibilias.home.ui.fragment.UserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {
    override val layoutId = R.layout.activity_user_info

    lateinit var fragmentTransaction:FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mid = intent.getLongExtra("mid", BaseApplication.asUser.mid)

        enableEdgeToEdge()

        val fragmentManager: FragmentManager = supportFragmentManager

        fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = UserFragment.newInstance()

        fragment.arguments = Bundle().apply {
            putLong("mid", mid)
        }

        fragmentTransaction.replace(binding.userInfoPageFragment.id, fragment)

        fragmentTransaction.commit()
    }


    companion object {
        fun actionStart(context: Context, mid: Long?) {
            val intent = Intent(context, UserInfoActivity::class.java)
            intent.putExtra("mid", mid)
            context.startActivity(intent)
        }

    }
}