package com.mitsui.tkmid.inventorycheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mitsui.tkmid.inventorycheck.databinding.FragmentInputBinding


class InputFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInputBinding>(
            inflater,
            R.layout.fragment_input, container, false
        )
        return binding.root
    }

}
