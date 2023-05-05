package bocardo.fernando.mydigimind.ui.dashboard

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bocardo.fernando.mydigimind.R
import java.text.SimpleDateFormat
import java.util.*
import bocardo.fernando.mydigimind.databinding.FragmentDashboardBinding
import bocardo.fernando.mydigimind.ui.Task
import bocardo.fernando.mydigimind.ui.home.HomeFragment
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val baseDatos = FirebaseFirestore.getInstance()

        val btn_time: Button = root.findViewById(R.id.btn_time)
        btn_time.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE, minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(root.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val done: Button = root.findViewById(R.id.done)
        val et_titulo: EditText = root.findViewById(R.id.name)
        val monday: CheckBox = root.findViewById(R.id.monday)
        val tuesday: CheckBox = root.findViewById(R.id.tuesday)
        val wednesday: CheckBox = root.findViewById(R.id.wednesday)
        val thursday: CheckBox = root.findViewById(R.id.thursday)
        val friday: CheckBox = root.findViewById(R.id.friday)
        val saturday: CheckBox = root.findViewById(R.id.saturday)
        val sunday: CheckBox = root.findViewById(R.id.sunday)
        done.setOnClickListener{
            var title = et_titulo.text.toString()
            var time = btn_time.text.toString()
            var mondaySelected = monday.isChecked
            var tuesdaySelected = tuesday.isChecked
            var wednesdaySelected = wednesday.isChecked
            var thursdaySelected = thursday.isChecked
            var fridaySelected = friday.isChecked
            var saturdaySelected = saturday.isChecked
            var sundaySelected = sunday.isChecked
            var task = hashMapOf(
                "actividad" to title,
                "lu" to mondaySelected,
                "ma" to tuesdaySelected,
                "mi" to wednesdaySelected,
                "ju" to thursdaySelected,
                "vi" to fridaySelected,
                "sa" to saturdaySelected,
                "do" to sundaySelected,
                "tiempo" to time
            )

            baseDatos.collection("actividades")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(root.context, "New task added!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(root.context, "Error adding task", Toast.LENGTH_SHORT).show()
                }


        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}