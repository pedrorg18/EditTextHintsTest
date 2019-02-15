package com.pedroroig.example.edittexthintstest

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.reactivex.SingleObserver
import java.lang.Exception
import java.lang.IllegalArgumentException

class HintsAdapter(private val deleteHintsObserver: SingleObserver<String>) : RecyclerView.Adapter<HintsAdapter.ViewHolder>() {

    var dataSet: MutableList<String>? = null
    
    private val tag = "HINTS-ADAPTER"

    override fun onCreateViewHolder(viewRoot: ViewGroup, viewType: Int): ViewHolder {
        Log.i(tag, "onCreateViewHolder()")
        return ViewHolder(
            LayoutInflater.from(viewRoot.context)
                .inflate(R.layout.hint_item, viewRoot, false)
        )
    }

    override fun getItemCount() = dataSet?.size ?: 0

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val element = if (dataSet != null && !dataSet?.get(position).isNullOrEmpty())
            dataSet?.get(position)
        else
            throw IllegalArgumentException("Hint should not be null")
        Log.i(tag, "onBindViewHolder() - element: $element")

        with(vh) {
            text.text = element

            deleteImage.setOnClickListener {
                Log.i(tag, "removing hint() - element: $element - position: $position")
                try {
                    dataSet!!.remove(element)
                    notifyDataSetChanged()
                    deleteHintsObserver.onSuccess(element!!)
                }catch (e: Exception) {
                    deleteHintsObserver.onError(e)
                }
            }

            rootView.setOnClickListener {
                Toast.makeText(rootView.context, "Click on $element", Toast.LENGTH_LONG).show()
            }
        }
    }


    class ViewHolder(viewRoot: View) :
        RecyclerView.ViewHolder(viewRoot) {
        val rootView = viewRoot
        val text: TextView = viewRoot.findViewById(R.id.tvHint)
        val deleteImage: ViewGroup = viewRoot.findViewById(R.id.containerDeleteHint)
    }

}
