package com.moneyburndown.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.moneyburndown.extensions.fragmentsProvider
import com.moneyburndown.extensions.fromComponents
import com.moneyburndown.extensions.myActivity
import com.moneyburndown.extensions.toCalendar
import com.moneyburndown.viewmodel.LimitViewModel
import com.moneyburndown.viewmodel.LimitViewModelFactory
import org.koin.android.ext.android.inject
import java.util.*

class LimitDateDialog : DialogFragment() {

    private val viewModelFactory: LimitViewModelFactory by inject()
    private lateinit var viewModel: LimitViewModel

    private val listener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
        viewModel.onDateSelected(fromComponents(year, month, day))
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = myActivity.fragmentsProvider(SetLimitDialog::class.java, viewModelFactory) [LimitViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.get(ARG_DATE) as Calendar
        val minDate = arguments?.get(ARG_MIN_DATE) as Calendar
        val maxDate = arguments?.get(ARG_MAX_DATE) as Calendar?

        return DatePickerDialog(myActivity, listener, date[Calendar.YEAR], date[Calendar.MONTH], date[Calendar.DAY_OF_MONTH]).apply {
            datePicker.minDate = minDate.timeInMillis
            maxDate?.run { datePicker.maxDate = timeInMillis }
        }
    }

    companion object {

        private const val ARG_DATE = "arg_date"
        private const val ARG_MIN_DATE = "arg_min_date"
        private const val ARG_MAX_DATE = "arg_max_date"

        fun create(
            date: Date,
            minDate: Date,
            maxDate: Date?
        ): LimitDateDialog = LimitDateDialog().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DATE, date.toCalendar())
                putSerializable(ARG_MIN_DATE, minDate.toCalendar())
                putSerializable(ARG_MAX_DATE, maxDate?.toCalendar())
            }
        }
    }
}