package pl.ozodbek.sqlpractices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class StudentActivity : AppCompatActivity() {
    private lateinit var myDbHelper: MyDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        myDbHelper = MyDbHelper(this)

        val id = intent.getIntExtra("id", 0)
        val student = myDbHelper.getStudentById(id)
        Toast.makeText(this, "$student", Toast.LENGTH_SHORT).show()
    }
}