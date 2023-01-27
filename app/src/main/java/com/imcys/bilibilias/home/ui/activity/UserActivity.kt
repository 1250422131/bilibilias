package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.imcys.bilibilias.base.BaseComponentActivity
import com.imcys.bilibilias.home.ui.activity.layout.UserActivityLayout
import com.imcys.bilibilias.home.ui.model.view.UserViewModel

class UserActivity : BaseComponentActivity() {


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            //引入布局
            val viewModel = UserViewModel()
            UserActivityLayout(viewModel)
        }


    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }
    }

}







