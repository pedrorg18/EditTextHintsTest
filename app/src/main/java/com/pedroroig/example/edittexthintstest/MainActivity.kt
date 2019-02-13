package com.pedroroig.example.edittexthintstest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View.*
import android.widget.Toast
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val tag = "HINTS-ACTIVITY"

    private lateinit var adapter: HintsAdapter
    private val hintsRepository = HintsRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpHintRecyclerView()

        setUpListeners()
    }

    private fun setUpHintRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rvHints.layoutManager = layoutManager
        adapter = HintsAdapter(deleteHintsObserver)
        rvHints.adapter = adapter
        refreshHintsAdapterData()
    }

    private fun setUpListeners() {
        buttonSave.setOnClickListener {
            if(etInput.text.isNotEmpty()) {
                val hint = etInput.text.toString().trim().toUpperCase()
                hintsRepository.storeHint(hint)
                refreshHintsAdapterData()
            }
        }

        etInput.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && hintsRepository.getHints().isNotEmpty()) {
                refreshHintsAdapterData()
            } else {
                rvHints.visibility = GONE
            }
        }
    }

    private fun refreshHintsAdapterData() {
        val hints = hintsRepository.getHints()
        Log.i(tag, "refreshHintsAdapterData - List: $hints")
        adapter.dataSet = hints
        adapter.notifyDataSetChanged()

        if(hints.isEmpty())
            rvHints.visibility = GONE
        else
            rvHints.visibility = VISIBLE
    }


    private val deleteHintsObserver: SingleObserver<String> = object: SingleObserver<String> {
        override fun onSuccess(hint: String) {
            hintsRepository.deleteHint(hint)
            refreshHintsAdapterData()
        }

        override fun onSubscribe(d: Disposable) {}

        override fun onError(e: Throwable) {
            Toast.makeText(this@MainActivity, "There was an error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }


}
