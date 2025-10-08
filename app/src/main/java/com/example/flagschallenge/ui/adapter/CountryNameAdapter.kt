package com.example.flagschallenge.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flagschallenge.R
import com.example.flagschallenge.databinding.LayoutCountryListBinding
import com.example.flagschallenge.model.CountryList

class CountryNameAdapter(val list: MutableList<CountryList.Country>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAnswerUpdated = false
    lateinit var context: Context
    private var answerId = -1

    @SuppressLint("NotifyDataSetChanged")
    fun updateNextList(newList: MutableList<CountryList.Country>) {
        isAnswerUpdated = false
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAnswer(mAnswerId: Int = -1, userSelectPos: Int = -1) {
        answerId = mAnswerId
        if (answerId != -1) {
            isAnswerUpdated = true
            for (i in list) {
                if (userSelectPos != -1 && i.id == list[userSelectPos].id){
                    list[userSelectPos].userSelect = true
                }
                if (i.id == answerId) {
                    i.correctAnswer = true
                    break
                }
            }
        } else if (userSelectPos != -1) {
            for (i in 0 until list.size){
                list[i].userSelect = i == userSelectPos
            }
            //notifyItemChanged(userSelectPos)
        }
        notifyDataSetChanged()
    }

    var onItemClick: ((pos: Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val viewBind = LayoutCountryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
//    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_country_list,parent,false)
        return ViewHolder(viewBind)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        //val itemView = holder as ViewHolder
        (holder as ViewHolder).bind(position, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(val view: LayoutCountryListBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(pos: Int, item: CountryList.Country) {
            view.tvAnswer.text = item.country_name
            view.tvAnswerStatus.visibility = View.INVISIBLE

            if (isAnswerUpdated){
                if (item.correctAnswer){
                    view.tvAnswer.background = ContextCompat.getDrawable(context,R.drawable.bg_border_rounded_green)
                    view.tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.text_other_color))
                    view.tvAnswerStatus.visibility = View.VISIBLE
                    view.tvAnswerStatus.text = context.resources.getString(R.string.lbl_correct)
                    view.tvAnswerStatus.setTextColor(ContextCompat.getColor(context, R.color.textColorGreen))
                }
//                if (item.userSelect && answerId != item.id){
                if (!item.correctAnswer && item.userSelect){
                    view.tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
                    view.tvAnswer.background = ContextCompat.getDrawable(context,R.drawable.bg_border_rounded_filled_orange)
                    view.tvAnswerStatus.visibility = View.VISIBLE
                    view.tvAnswerStatus.text = context.resources.getString(R.string.lbl_wrong)
                    view.tvAnswerStatus.setTextColor(ContextCompat.getColor(context, R.color.orange))
                }

            } else {
                if (item.userSelect){
                    view.tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
                    view.tvAnswer.background = ContextCompat.getDrawable(context,R.drawable.bg_border_rounded_filled_orange)
                } else {
                    view.tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.text_other_color))
                    view.tvAnswer.background = ContextCompat.getDrawable(context,R.drawable.bg_border_rounded_black)
                }
            }

            view.tvAnswer.setOnClickListener {
               if (!isAnswerUpdated)
               {
                   onItemClick?.invoke(pos)
               }
            }

        }
    }
}