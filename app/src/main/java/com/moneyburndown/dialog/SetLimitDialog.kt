package com.moneyburndown.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moneyburndown.R
import com.moneyburndown.databinding.DialogSetLimitBinding
import com.moneyburndown.view.Confirm
import com.moneyburndown.view.Exit
import com.moneyburndown.view.SelectDate
import com.moneyburndown.viewmodel.LimitViewModel
import com.moneyburndown.viewmodel.LimitViewModelFactory
import org.koin.android.ext.android.inject

class SetLimitDialog : BottomSheetDialogFragment() {

    private val viewModelFactory: LimitViewModelFactory by inject()

    private lateinit var viewModel: LimitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[LimitViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_set_limit, container, false)
        val binding = DialogSetLimitBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SelectDate -> LimitDateDialog.create(it.date, it.minDate, it.maxDate)
                    .show(activity?.supportFragmentManager, LimitDateDialog::class.java.simpleName)
                Exit -> dismiss()
                Confirm -> ResetConfirmDialog().show(fragmentManager, ResetConfirmDialog::class.java.simpleName)
            }
        })
    }
}