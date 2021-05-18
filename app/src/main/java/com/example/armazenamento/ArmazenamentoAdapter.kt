package com.example.armazenamento

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.armazenamento_card.view.*

class ArmazenamentoAdapter(private var arm_list: List<Armazenamento>, private var listener: MainActivity) : RecyclerView.Adapter<ArmazenamentoAdapter.ArmazenamentoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmazenamentoHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.armazenamento_card, parent,
            false)

        return ArmazenamentoHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArmazenamentoHolder, position: Int) {
        val currentItem = arm_list[position]

        holder.armTitleView.text = currentItem.arm_title
    }

    override fun getItemCount() = arm_list.size

    inner class ArmazenamentoHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        val armTitleView: TextView =  itemView.text_view_arm_name

        init {
            itemView.image_view_delete.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            var isDeleted = v?.id  == R.id.image_view_delete
            if(position != RecyclerView.NO_POSITION){
                if(isDeleted){
                    listener.onDeleteItemClick(position)
                }else{
                    listener.onItemClick(position)
                }

            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onDeleteItemClick(position: Int)
    }
}