package com.imcys.bilibilias.home.ui.activity.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.databinding.ActivityUserVideoDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.UserVideoDownloadAdapter
import com.imcys.bilibilias.home.ui.viewmodel.UserVideoDownloadViewModel
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserVideoDownloadActivity : BaseActivity() {

    lateinit var binding: ActivityUserVideoDownloadBinding

    private val viewModel: UserVideoDownloadViewModel by viewModels<UserVideoDownloadViewModel>()
    private lateinit var loadDialog: BottomSheetDialog

    private lateinit var userVideoDownloadAdapter: UserVideoDownloadAdapter

    @Inject
    lateinit var networkService: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserVideoDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
        bindingEvent()
        initUserList()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {

        binding.apply {
            uvDownloadTopLy.addStatusBarTopPadding()
        }

        userVideoDownloadAdapter = UserVideoDownloadAdapter()
        binding.apply {
            userVideoRv.adapter = userVideoDownloadAdapter
            userVideoRv.layoutManager = LinearLayoutManager(this@UserVideoDownloadActivity)

            userVideoDownloadAdapter.editStateChange = {
                if (it) {
                    uvDownloadTopEdit.visibility = View.VISIBLE
                    uvDownloadButton.visibility = View.VISIBLE

                } else {
                    uvDownloadTopEdit.visibility = View.GONE
                    uvDownloadButton.visibility = View.GONE

                }
            }


            uvDownloadEditSelectAll.setOnClickListener {
                userVideoDownloadAdapter.currentList.forEachIndexed { index, _ ->
                    userVideoDownloadAdapter.selectList.add(index)
                }
                userVideoDownloadAdapter.notifyDataSetChanged()
            }


            uvDownloadEditInvert.setOnClickListener {
                val oldList = userVideoDownloadAdapter.selectList
                val newList = mutableListOf<Int>()
                userVideoDownloadAdapter.currentList.forEachIndexed { index, _ ->
                    if (!oldList.contains(index)) {
                        newList.add(index)
                    }
                }
                userVideoDownloadAdapter.selectList.clear()
                userVideoDownloadAdapter.selectList.addAll(newList)
                userVideoDownloadAdapter.notifyDataSetChanged()
            }


            uvDownloadEditCancel.setOnClickListener {
                userVideoDownloadAdapter.showState = false
                userVideoDownloadAdapter.notifyDataSetChanged()
                uvDownloadTopEdit.visibility = View.GONE
                uvDownloadButton.visibility = View.GONE
            }

            uvDownloadButton.setOnClickListener {
                val selectList = userVideoDownloadAdapter.selectList
                val currentList = userVideoDownloadAdapter.currentList

                if (selectList.isNotEmpty()) {
                    DialogUtils.batchDownloadVideoDialog(
                        this@UserVideoDownloadActivity,
                        currentList.filterIndexed { index, _ -> selectList.contains(index) },
                        networkService,
                    ).show()
                }
            }

        }

    }

    private fun bindingEvent() {

        loadDialog = DialogUtils.loadProgressDialog(this)

        viewModel.loadInfo.observe(this) {

            val tip = loadDialog.findViewById<TextView>(R.id.dialog_load_tip)
            val progressBar = loadDialog.findViewById<ProgressBar>(R.id.dialog_load_progressBar)

            tip?.apply {
                text = it.tip
            }

            progressBar?.apply {
                progress = it.progression
            }

            if (it.loadState) {
                loadDialog.show()
            } else {
                loadDialog.cancel()
            }


        }


        viewModel.userVideo.observe(this) {
            userVideoDownloadAdapter.submitList(it)
        }


    }

    private fun initUserList() {
        val mid = intent.getLongExtra("mid", BaseApplication.asUser.mid)
        viewModel.loadUserAllVideo(mid.toString())
    }


    companion object {
        fun actionStart(context: Context, mid: Long?) {
            val intent = Intent(context, UserVideoDownloadActivity::class.java)
            intent.putExtra("mid", mid)
            context.startActivity(intent)
        }


    }
}