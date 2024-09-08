import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PieChartViewModel : ViewModel() {

    // LiveData to observe pie chart data changes
    private val _data = MutableLiveData<MutableMap<String, Float>>()
    val data: LiveData<MutableMap<String, Float>> get() = _data

    // Update data method
    fun updateData(newData: MutableMap<String, Float>) {
        _data.value = newData
    }
}
