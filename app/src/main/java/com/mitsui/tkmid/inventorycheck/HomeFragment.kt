package com.mitsui.tkmid.inventorycheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.mitsui.tkmid.inventorycheck.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home, container, false
        )

        binding.qrReaderButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_QRReaderFragment)
        }
        binding.inputButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_inputFragment)
        }
        binding.listButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_listFragment)
        }

        return binding.root
    }

}
