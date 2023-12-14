@file:Suppress("SpellCheckingInspection")

package pl.ozodbek.sqlpractices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import pl.ozodbek.sqlpractices.adapters.StudentAdapter
import pl.ozodbek.sqlpractices.databinding.ActivityMainBinding
import pl.ozodbek.sqlpractices.databinding.StudentDialogBinding
import pl.ozodbek.sqlpractices.models.Student

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myDbHelper: MyDbHelper

    private lateinit var studentList: ArrayList<Student>
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)
        studentList = ArrayList(myDbHelper.listOfStudents())



        binding.apply {
            applyButton.setOnClickListener {

                val name = nameEt.text.toString()
                val age = ageEt.text.toString().toInt()
                val phoneNumber = phoneNumberEt.text.toString()

                val student = Student(name = name, age = age, phone_number = phoneNumber)
                myDbHelper.addStudent(student)
                studentList.add(student)
                studentAdapter.notifyItemInserted(studentList.size)
            }

            studentAdapter =
                StudentAdapter(studentList, { student, position ->
                    myDbHelper.deleteStudent(student)
                    studentList.remove(student)
                    studentAdapter.notifyItemRemoved(position)
                    studentAdapter.notifyItemRangeChanged(position, studentList.size)
                }, { student, position ->
                    showEditingDialog(student, position)

                },{
                    student ->
                    startActivity(Intent(this@MainActivity, StudentActivity::class.java)
                        .putExtra("id", student.id))
                })

            recyclerView.adapter = studentAdapter
        }


    }

    private fun showEditingDialog(student: Student, position: Int) {
        val alertDialog = AlertDialog.Builder(this)
        val studentDialogBinding = StudentDialogBinding.inflate(layoutInflater)
        alertDialog.setView(studentDialogBinding.root)
        val dialog = alertDialog.create()
        studentDialogBinding.apply {
            nameDialogEt.setText(student.name)
            ageDialogEt.setText(student.age.toString())
            phoneNumberDialogEt.setText(student.phone_number)

            saveButton.setOnClickListener {
                val newName = nameDialogEt.text.toString()
                val newAge = ageDialogEt.text.toString().toInt()
                val newPhoneNumber = phoneNumberDialogEt.text.toString()

                student.name = newName
                student.age = newAge
                student.phone_number = newPhoneNumber
                myDbHelper.editStudent(student)
                studentAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}