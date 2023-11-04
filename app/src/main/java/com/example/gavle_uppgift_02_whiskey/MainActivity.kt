package com.example.gavle_uppgift_02_whiskey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var selectedWhisky = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val whiskies = resources.getStringArray(R.array.whiskies)
        var infoText = findViewById<TextView>(R.id.textView);
        val spinner: Spinner = findViewById(R.id.spinner)
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, whiskies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,
                                        id: Long) {
                var selectedIndex = spinner.getSelectedItemPosition();
                if (selectedIndex > 0)
                    selectedWhisky = whiskies[selectedIndex]
                else
                    selectedWhisky = ""
                printInfo(infoText)
            }
        }
    }
    fun printInfo(tView: TextView) {
        if (!selectedWhisky.equals(""))
            tView.text = "Your favourite whisky is " + selectedWhisky
        else
            tView.text = "Please choose your favourite whisky."
    }
}