package pl.ozodbek.sqlpractices

import pl.ozodbek.sqlpractices.models.Student

interface DataBaseServise {

    fun addStudent(student: Student)

    fun listOfStudents(): List<Student>

    fun editStudent(student: Student)

    fun deleteStudent(student: Student)

    fun getStudentsCount(): Int

    fun getStudentById(id: Int): Student

}