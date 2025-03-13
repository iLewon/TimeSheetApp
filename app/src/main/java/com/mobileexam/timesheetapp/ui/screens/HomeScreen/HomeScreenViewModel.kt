import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val _isClockedIn = MutableStateFlow(false)
    val isClockedIn = _isClockedIn.asStateFlow()

    private val _isOnBreak = MutableStateFlow(false)
    val isOnBreak = _isOnBreak.asStateFlow()

    private val _dutySeconds = MutableStateFlow(0)
    val dutySeconds = _dutySeconds.asStateFlow()

    private var dutyTimerJob: Job? = null

    fun clockIn() {
        _isClockedIn.value = true
        _dutySeconds.value = 0
        startDutyTimer()
    }

    fun clockOut() {
        _isClockedIn.value = false
        _isOnBreak.value = false
        stopDutyTimer()
        _dutySeconds.value = 0
    }

    fun toggleBreak() {
        _isOnBreak.value = !_isOnBreak.value
    }

    fun startDutyTimer() {
        stopDutyTimer() // Cancel any existing timer before starting a new one

        dutyTimerJob = viewModelScope.launch {
            while (_isClockedIn.value) {
                delay(1000)
                _dutySeconds.update { it + 1 } // Safely update dutySeconds
            }
        }
    }

    fun stopDutyTimer() {
        dutyTimerJob?.cancel()
        dutyTimerJob = null
    }
}
