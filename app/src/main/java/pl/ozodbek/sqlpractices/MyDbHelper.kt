@file:Suppress("SpellCheckingInspection")

package pl.ozodbek.sqlpractices

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pl.ozodbek.sqlpractices.models.Student

class MyDbHelper(context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
), DataBaseServise {
    companion object {
        const val DB_NAME = "my_exampleDB"
        const val DB_VERSION = 1

        const val TABLE_NAME = "Student"
        const val STUDENT_ID = "id"
        const val STUDENT_NAME = "name"
        const val STUDENT_AGE = "age"
        const val PHONE_NUMBER = "phone_number"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // database yaratilgandan so'ng ishga tushadi...
        val query = "CREATE TABLE " +
                "$TABLE_NAME(" +
                "$STUDENT_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$STUDENT_NAME TEXT NOT NULL, " +
                "$STUDENT_AGE INTEGER NOT NULL, " +
                "$PHONE_NUMBER TEXT NOT NULL)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // database versiyasi o'zgartirilsa ishga tushadi...
    }


    override fun addStudent(student: Student) {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(STUDENT_NAME, student.name)
            put(STUDENT_AGE, student.age)
            put(PHONE_NUMBER, student.phone_number)
        }
        database.insert(TABLE_NAME, null, contentValues)
    }

    override fun listOfStudents(): List<Student> {
        val list = ArrayList<Student>()
        val query = "SELECT * FROM $TABLE_NAME"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val age = cursor.getInt(2)
                val phoneNumber = cursor.getString(3)
                val student = Student(id, name, age, phoneNumber)
                list.add(student)
            } while (cursor.moveToNext())
        }
        return list
    }

    override fun editStudent(student: Student) {
        val contentValues = ContentValues().apply {
            put(STUDENT_NAME, student.name)
            put(STUDENT_AGE, student.age)
            put(PHONE_NUMBER, student.phone_number)
        }
        this.writableDatabase.apply {
            update(
                TABLE_NAME,
                contentValues,
                "$STUDENT_ID = ?",
                arrayOf(student.id.toString())
            )
        }
    }

    override fun deleteStudent(student: Student) {
        this.writableDatabase.apply {
            delete(
                TABLE_NAME,
                "$STUDENT_ID = ?",
                arrayOf(student.id.toString())
            )
        }
    }

    override fun getStudentsCount(): Int {
        val database = this.writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(query, null)
        return cursor.count
    }

    override fun getStudentById(id: Int): Student {
        val database = this.readableDatabase
        val cursor = database.query(
            TABLE_NAME,
            arrayOf(STUDENT_ID, STUDENT_NAME, STUDENT_AGE, PHONE_NUMBER),
            "$STUDENT_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return Student(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3))
    }
}