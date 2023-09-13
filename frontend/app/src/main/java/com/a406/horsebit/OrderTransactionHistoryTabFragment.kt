package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentOrderTransactionHistoryTabBinding

class OrderTransactionHistoryTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderTransactionHistoryTabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_transaction_history_tab, container, false)

        binding = FragmentOrderTransactionHistoryTabBinding.bind(view)

        return view
    }
}