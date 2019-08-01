package com.bb.moneyburndown.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bb.moneyburndown.R
import com.bb.moneyburndown.databinding.DialogAddChangeBinding
import com.bb.moneyburndown.view.Exit
import com.bb.moneyburndown.viewmodel.ChangeViewModel
import com.bb.moneyburndown.viewmodel.ChangeViewModelFactory
import kotlinx.android.synthetic.main.dialog_add_change.*
import org.koin.android.ext.android.inject

class AddChangeDialog : DialogFragment() {

    private val viewModelFactory: ChangeViewModelFactory by inject()
    private lateinit var viewModel: ChangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[ChangeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_change, container, false)
        val binding = DialogAddChangeBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                Exit -> dismiss()
            }
        })

        edit_value.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    viewModel.exit(true)
                    true
                }
                else -> false
            }
        }
        edit_value.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
    }
}