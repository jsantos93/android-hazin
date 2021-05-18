package com.example.armazenamento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*

class ArmazenamentoAdapter(private var arm_list: List<Armazenamento>, private var listener: MainActivity) : RecyclerView.Adapter<ArmazenamentoAdapter.ArmazenamentoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmazenamentoHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.armazenamento_card, parent,
            false)

        return ArmazenamentoHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArmazenamentoHolder, position: Int) {
        val currentItem = arm_list[position]

        holder.armTitleView.text = currentItem.arm_title
        holder.armContentView.text = currentItem.arm_content
        holder.radioButtonView.text = currentItem.storage_type

    }

    override fun getItemCount() = arm_list.size

    inner class ArmazenamentoHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val armTitleView: TextView =  itemView.armTitle
        val armContentView: TextView =  itemView.armContent
        val radioButtonView: RadioButton = itemView.radio_internal

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}