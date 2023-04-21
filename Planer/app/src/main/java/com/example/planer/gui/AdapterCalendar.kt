package com.example.planer.gui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.databinding.SingleEventBinding
import com.example.planer.databinding.SingleTaskBinding
import com.example.planer.entities.Calendar
import com.example.planer.entities.Tasks
import kotlinx.android.synthetic.main.single_event.view.*
import kotlinx.android.synthetic.main.single_task.view.*

class AdapterCalendar (
    var list: MutableList<Calendar>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit,
) : RecyclerView.Adapter<AdapterCalendar.ViewHolder>() {


    class ViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_event, parent, false) as CardView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.title_event.text = item.name
        holder.itemView.date_event.text = item.startDate
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newCalendar: MutableList<Calendar>) {
        val diffResult = DiffUtil.calculateDiff(
            com.example.planer.gui.callBacks.CalendarDiffCallback(
                this.list,
                newCalendar
            )
        )
        this.list = newCalendar
        diffResult.dispatchUpdatesTo(this)
    }

}

