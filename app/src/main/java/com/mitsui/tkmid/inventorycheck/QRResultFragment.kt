package com.mitsui.tkmid.inventorycheck

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.mitsui.tkmid.inventorycheck.databinding.FragmentQrResultBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat


class QRResultFragment : Fragment() {

    private lateinit var inventoryCheckDao: InventoryCheckDao
    private lateinit var db: InventoryCheckDatabase
    var coroutineJob = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + coroutineJob)
    private lateinit var inventoryCheckItem: InventoryCheckItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("CallCheck", "QR Result Fragment entered")
        val binding = DataBindingUtil.inflate<FragmentQrResultBinding>(
            inflater,
            R.layout.fragment_qr_result, container, false
        )

        binding.goToHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_QRResultFragment_to_homeFragment)
        }
        binding.nextButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_QRResultFragment_to_QRReaderFragment)
        }

        db = getContext()?.let { InventoryCheckDatabase.getInstance(it) }!!
        inventoryCheckDao = db.inventoryCheckDao

        runBlocking {
            val job = uiScope.launch {
                inventoryCheckItem = inventoryCheckDao.getLastItem()!!
            }
            job.join()
        }

        if (inventoryCheckItem != null) {
            binding.resultText1.text = "登録日時： " + SimpleDateFormat("yyyy/MM/dd HH:mm")
                .format(inventoryCheckItem.checkedTime).toString() + "\nQRコード値："
            binding.resultText2.text = inventoryCheckItem.qrCodeValue
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineJob.cancel()
    }

}
