package com.example.planer.gui.pages.settings

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Types
import com.example.planer.gui.callBacks.TypeDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TypeAdapter(typeList: List<Types>) :
    RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    private var types: List<Types> = typeList
    private var onButtonClickListener: OnButtonClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeName: TextView
        val chosenColorButton: Button

        init {
            typeName = view.findViewById(R.id.typeName)
            chosenColorButton = view.findViewById(R.id.colorPickerButton)
        }

    }

    interface OnButtonClickListener {
        suspend fun onChosenColorButtonClick(type: Types, holder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.settings_type_color_selection, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val type = types[position]

        holder.typeName.text = type.name
        holder.chosenColorButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor(type.colour))

        holder.chosenColorButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                onButtonClickListener?.onChosenColorButtonClick(type, holder)
            }
        }
    }

    fun setOnItemClickListener(listener: UserSettingsActivity) {
        onButtonClickListener = listener
    }

    fun updateList(newTypes: List<Types>) {
        val diffResult = DiffUtil.calculateDiff(TypeDiffCallback(types, newTypes))
        types = newTypes
        diffResult.dispatchUpdatesTo(this)
    }
}