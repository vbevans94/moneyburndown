package com.moneyburndown.view

import java.util.*

sealed class ViewEvent

data class SelectDate(val date: Date, val minDate: Date, val maxDate: Date?) : ViewEvent()

object AddChange : ViewEvent()

object SetLimit : ViewEvent()

object Exit : ViewEvent()

object Confirm : ViewEvent()

object About : ViewEvent()


