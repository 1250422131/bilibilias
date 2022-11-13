package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.FragmentUserBinding


class UserFragment : Fragment() {



    //
    lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        return fragmentUserBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}