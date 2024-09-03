package com.nicco.willowthreedemo.presentation

import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nicco.willowthreedemo.R
import com.nicco.willowthreedemo.data.model.UserResponse
import com.nicco.willowthreedemo.presentation.model.UserWillowUi

class GameAdapter(
    private val dataSet: List<UserWillowUi>,
    private val callback: (UserWillowUi) -> Unit
) :
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_view_item, parent, false) as ConstraintLayout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val img = holder.itemView.findViewById<AppCompatImageView>(R.id.img)
        val imgAnswer = holder.itemView.findViewById<AppCompatImageView>(R.id.imgAnswer)

        img.load(dataSet[position].img) {
            placeholder(R.drawable.ic_android_black_24dp)
        }

        img.setOnClickListener {
            if (position == 0) {
                val context = holder.itemView.context
                imgAnswer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.green_correct_answer)
                )
                imgAnswer.setImageResource(R.drawable.ic_correct_answer)
            } else {
                val context = holder.itemView.context
                imgAnswer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.red_incorrect_answer)
                )
                imgAnswer.setImageResource(R.drawable.ic_incorrect_anwer)
            }

            Handler().postDelayed(object : Runnable {
                override fun run() {
                    callback.invoke(dataSet[position])
                }
            }, 100)
        }
    }

    override fun getItemCount(): Int {
        return kotlin.math.min(dataSet.size, 6)
    }
}