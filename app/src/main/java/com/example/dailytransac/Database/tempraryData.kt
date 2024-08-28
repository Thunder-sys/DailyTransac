import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel

class MyViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    var textValue: String?
        get() = sharedPreferences.getString("text_value", null)
        set(value) {
            sharedPreferences.edit {
                putString("text_value", value)
            }
        }

    var passwork: String?
        get() = sharedPreferences.getString("passwork", null)
        set(value) {
            sharedPreferences.edit {
                putString("passwork", value)
            }
        }
}
