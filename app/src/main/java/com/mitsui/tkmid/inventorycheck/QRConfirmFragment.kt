package com.mitsui.tkmid.inventorycheck

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.mitsui.tkmid.inventorycheck.databinding.FragmentQrConfirmBinding
import kotlinx.coroutines.*


class QRConfirmFragment : Fragment() {

    private lateinit var inventoryCheckDao: InventoryCheckDao
    private lateinit var db: InventoryCheckDatabase
    var coroutineJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + coroutineJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val arguments = QRConfirmFragmentArgs.fromBundle(arguments!!)

        val binding = DataBindingUtil.inflate<FragmentQrConfirmBinding>(
            inflater,
            R.layout.fragment_qr_confirm, container, false
        )

        val qrReadValue: String = arguments.qrReadValue.toString()
        binding.qrReadValue.text = qrReadValue

        db = getContext()?.let { InventoryCheckDatabase.getInstance(it) }!!
        inventoryCheckDao = db.inventoryCheckDao
        val item = InventoryCheckItem()
        item.qrCodeValue = qrReadValue

        binding.registButton.setOnClickListener { view: View ->
            runBlocking {
                val job = uiScope.launch {
                    inventoryCheckDao.insert(item)
                    Log.i("CallCheck", "insertItem executed")
                }
                job.join()
                view.findNavController().navigate(R.id.action_QRConfirmFragment_to_QRResultFragment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineJob.cancel()
    }

}
