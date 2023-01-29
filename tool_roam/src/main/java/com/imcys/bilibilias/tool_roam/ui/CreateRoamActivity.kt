package com.imcys.bilibilias.tool_roam.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.drake.statelayout.StateLayout
import com.drake.statelayout.stateCreate
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.entity.RoamInfo
import com.imcys.bilibilias.tool_roam.R
import com.imcys.bilibilias.tool_roam.base.RoamBaseActivity
import com.imcys.bilibilias.tool_roam.databinding.ActivityCreateRoamBinding
import com.xiaojinzi.component.Component
import com.xiaojinzi.component.anno.RouterAnno
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@RouterAnno(
    hostAndPath = ARouterAddress.CreateRoamActivity
)

class CreateRoamActivity : RoamBaseActivity() {


    lateinit var appDatabase: AppDatabase
    lateinit var binding: ActivityCreateRoamBinding
    var cookieState = true
    private lateinit var bRVState: StateLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Component.inject(target = this)
        bRVState = stateCreate()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_roam)
        setSupportActionBar(binding.createRoamToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        binding.apply {
            createRoamTopLy.addStatusBarTopPadding()
            createRoamRadioGroup.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.create_roam_radio_cookie_true -> {
                        BaseApplication.sharedPreferences.edit {
                            putBoolean("use_roam_cookie_state", true)
                        }
                        cookieState = true
                    }
                    R.id.create_roam_radio_cookie_false -> {
                        BaseApplication.sharedPreferences.edit {
                            putBoolean("use_roam_cookie_state", false)
                        }
                        cookieState = false
                    }
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.roam_create_roam_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.create_roam_finish -> editFinish()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun editFinish() {

        binding.apply {

            if (createRoamNameEditText.text.toString() == "") {
                asToast(this@CreateRoamActivity, "请输入漫游名称")
                return
            }
            if (createRoamPathEditText.text.toString() == "") {
                asToast(this@CreateRoamActivity, "请输入解析地址")
                return
            }


            val roamName = createRoamNameEditText.text.toString()
            val roamPath = createRoamPathEditText.text.toString()
            val roamRemark = if (createRoamRemarkEditText.text != null) {
                createRoamRemarkEditText.text.toString()
            } else ""

            bRVState.showLoading()
            //检查编辑数据
            checkEditData(roamName, roamPath, roamRemark, cookieState)


        }

    }

    private fun checkEditData(
        roamName: String,
        roamPath: String,
        roamRemark: String,
        cookieState: Boolean,
    ) {


        lifecycleScope.launch(Dispatchers.IO) {

            HttpUtils.addHeader("cookie", BaseApplication.cookies)
                .get(roamPath + "pgc/player/web/playurl?cid=674487&qn=64&fnval=0&fourk=1",
                    object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                asToast(this@CreateRoamActivity, "该地址无法使用")
                                bRVState.showContent()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val str = response.body?.string()

                            lifecycleScope.launch(Dispatchers.Main) {
                                str?.apply {
                                    val strJson = JSONObject(str)
                                    val code = strJson.optInt("code")
                                    if (code == 0) handleEditData(roamName,
                                        roamPath,
                                        roamRemark,
                                        cookieState) else {
                                        asToast(this@CreateRoamActivity, "接口异常，无法正确返回数据")
                                    }
                                } ?: apply {
                                    asToast(this@CreateRoamActivity, "接口异常，无法正确返回数据")
                                    bRVState.showContent()
                                }
                            }

                        }

                    })

        }


    }


    /**
     * 处理编写数据
     * @param roamName String
     * @param roamPath String
     * @param roamRemark String
     */
    private fun handleEditData(
        roamName: String,
        roamPath: String,
        roamRemark: String,
        cookieState: Boolean,
    ) {

        lifecycleScope.launch(Dispatchers.IO) {
            appDatabase = AppDatabase.getDatabase(this@CreateRoamActivity)
            val roamDao = appDatabase.roamDao()

            //判断是否存在
            if (roamDao.isNameExist(roamName) != null) {
                lifecycleScope.launch(Dispatchers.Main) {
                    asToast(this@CreateRoamActivity,
                        "名称已经存在")
                    bRVState.showContent()
                }
                return@launch
            }

            //不存在可写入
            val roamInfo = RoamInfo(
                romaName = roamName,
                romaPath = roamPath,
                romaRemark = roamRemark,
                cookieState = cookieState
            )

            //储存数据
            roamDao.insert(roamInfo)

            lifecycleScope.launch(Dispatchers.Main) {
                bRVState.showContent()
                RoamMainActivity.actionStart(this@CreateRoamActivity)
            }

        }

    }
}