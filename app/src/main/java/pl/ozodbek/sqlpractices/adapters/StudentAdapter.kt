package pl.ozodbek.sqlpractices.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.sqlpractices.databinding.StudentRowItemBinding
import pl.ozodbek.sqlpractices.models.Student

class StudentAdapter(
    private val list: List<Student>,
    val onItemDelete: (Student, Int) -> Unit,
    val onItemEdit: (Student, Int) -> Unit,
    val onItemClick: (Student) -> Unit,
) :
    RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: StudentRowItemBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun onBind(student: Student, position: Int){
                binding.apply {
                    nameTextView.text = student.name

                    deleteButton.setOnClickListener {onItemDelete.invoke(student, position) }
                    editButton.setOnClickListener { onItemEdit.invoke(student, position) }
                    itemView.setOnClickListener { onItemClick.invoke(student) }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            StudentRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

}