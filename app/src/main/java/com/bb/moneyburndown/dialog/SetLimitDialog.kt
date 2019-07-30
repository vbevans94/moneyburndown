package com.bb.moneyburndown.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bb.moneyburndown.R
import com.bb.moneyburndown.databinding.DialogLimitBinding
import com.bb.moneyburndown.view.Confirm
import com.bb.moneyburndown.view.Exit
import com.bb.moneyburndown.view.SelectDate
import com.bb.moneyburndown.viewmodel.LimitViewModel

class SetLimitDialog : DialogFragment() {

    private lateinit var viewModel: LimitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[LimitViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_limit, container, false)
        val binding = DialogLimitBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SelectDate -> DateDialog.create(it.date, it.minDate, it.maxDate)
                    .show(activity?.supportFragmentManager, DateDialog::class.java.simpleName)
                Exit -> dismiss()
                Confirm -> ResetConfirmDialog().show(fragmentManager, ResetConfirmDialog::class.java.simpleName)
            }
        })
    }
}