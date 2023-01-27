package com.imcys.bilibilias.tool_roam.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.drake.brv.annotaion.AnimationType
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.statelayout.StateLayout
import com.drake.statelayout.stateCreate
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.dao.RoamDao
import com.imcys.bilibilias.common.data.entity.RoamInfo
import com.imcys.bilibilias.tool_roam.R
import com.imcys.bilibilias.tool_roam.base.RoamBaseActivity
import com.imcys.bilibilias.tool_roam.databinding.ActivityRoamMainBinding
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RouterAnno(
    hostAndPath = ARouterAddress.RoamMainActivity,
)
class RoamMainActivity : RoamBaseActivity() {
    private lateinit var bRVState: StateLayout
    private lateinit var roadDao: RoamDao
    var selectItem: Int = -1
    lateinit var binding: ActivityRoamMainBinding

    private var roamMutableList = mutableListOf<RoamInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_roam_main)

        binding.apply {

            roamMainTopNavigationLayout.addStatusBarTopPadding()

        }
        bRVState = stateCreate()

        setSupportActionBar(binding.roamMainToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        loadRoamList()

        initView()

    }

    private fun loadRoamList() {
        bRVState.showLoading()  // 加载中
        selectItem = BaseApplication.sharedPreferences.getInt("use_roam_id", -1)

        lifecycleScope.launch(Dispatchers.IO) {
            roadDao = AppDatabase.getDatabase(this@RoamMainActivity).roamDao().apply {
                val roams = getByIdOrderList()
                roams.forEach {
                    if (it.id == selectItem) it.selectState = 1
                }
                roamMutableList.addAll(roams)
                showBVList()
            }

        }


    }

    private fun showBVList() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.run {

                roamMainRv.linear().setup {
                    setAnimation(AnimationType.SCALE)
                    clickThrottle = 1000 // 覆盖全局设置
                    addType<RoamInfo>(R.layout.roam_item_roam)

                    onClick(R.id.item_roam_delete_bt) {
                        deleteItem(getModel())
                    }
                    onClick(R.id.item_roam_ly) {
                        saveSelectItem(getModel())
                    }

                }.models = roamMutableList

                bRVState.showContent()

            }
        }
    }

    private fun saveSelectItem(roamInfo: RoamInfo) {


        roamMutableList.forEach {
            if (it.id == roamInfo.id) {
                it.selectState = 1
                selectItem = roamInfo.id
            }
        }

        binding.roamMainRv.models = roamMutableList
        BaseApplication.sharedPreferences.edit {
            putInt("use_roam_id", selectItem)
        }
    }


    private fun initView() {

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.roam_main_tablayout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.main_menu_add -> {
                Router
                    .with(this)
                    .hostAndPath(hostAndPath = ARouterAddress.CreateRoamActivity).forward()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //栈内复用
        updateRoam()
    }

    private fun updateRoam() {
        lifecycleScope.launch(Dispatchers.IO) {
            val roams = roadDao.getByIdOrderList()
            roams.forEach {
                if (it.id == selectItem) it.selectState = 1
            }
            roamMutableList = roams
            lifecycleScope.launch(Dispatchers.Main) {
                binding.roamMainRv.models = roamMutableList
            }

        }

    }


    fun deleteItem(roamInfo: RoamInfo) {
        AsDialog.init(this)
            .setTitle("删除警告")
            .setContent("确定要删除这条纪录吗？")
            .setPositiveButton("确定") {
                //删除
                lifecycleScope.launch {
                    deleteRoam(this@RoamMainActivity, roamInfo)
                    it.cancel()
                }

            }
            .setNegativeButton("手滑了") {
                it.cancel()
            }.build().show()
    }

    /**
     * 删除纪录
     * @param roamInfo RoamInfo
     */
    private suspend fun deleteRoam(context: Context, roamInfo: RoamInfo) {
        roadDao.apply {
            if (roamInfo.id == selectItem){
                selectItem = -1
                BaseApplication.sharedPreferences.edit {
                    putInt("use_roam_id", selectItem)
                }
            }
            delete(roamInfo)
            updateRoam()
        }
    }


    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, RoamMainActivity::class.java)
            context.startActivity(intent)
        }
    }

}