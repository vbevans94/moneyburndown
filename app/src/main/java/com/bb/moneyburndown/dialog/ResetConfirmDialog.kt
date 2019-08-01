package com.bb.moneyburndown.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bb.moneyburndown.R
import com.bb.moneyburndown.extensions.fragmentsProvider
import com.bb.moneyburndown.extensions.myActivity
import com.bb.moneyburndown.viewmodel.LimitViewModel
import com.bb.moneyburndown.viewmodel.LimitViewModelFactory
import org.koin.android.ext.android.inject

class ResetConfirmDialog : DialogFragment() {

    private val viewModelFactory: LimitViewModelFactory by inject()
    private lateinit var viewModel: LimitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = myActivity.fragmentsProvider(SetLimitDialog::class.java, viewModelFactory) [LimitViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(myActivity)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.confirmSave(true)
            }
            .setNegativeButton(android.R.string.no) { _, _ ->
                viewModel.confirmSave(false)
            }
            .setTitle(R.string.title_confirm)
            .setMessage(R.string.message_reset_limit_confirmation)
            .create()
    }
}