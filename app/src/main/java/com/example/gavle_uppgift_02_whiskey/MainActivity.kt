package com.example.gavle_uppgift_02_whiskey

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var whiskyImage: ImageView
    private lateinit var artistImage: ImageView
    private lateinit var selectedWhiskyTextView: TextView
    private lateinit var selectedSizeTextView: TextView

    private lateinit var whiskySpinner: Spinner
    private lateinit var sizeSpinner: Spinner
    private lateinit var artistSpinner: Spinner

    private val whiskies by lazy { resources.getStringArray(R.array.whiskies) }
    private val sizes by lazy { resources.getStringArray(R.array.sizes) }
    private val artists by lazy { resources.getStringArray(R.array.artists) }
    private val pricesPerCl by lazy { resources.getIntArray(R.array.prices) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ImageViews and TextViews
        whiskyImage = findViewById(R.id.whiskyImage)
        artistImage = findViewById(R.id.artistImage)
        selectedWhiskyTextView = findViewById(R.id.selectedWhiskyTextView)
        selectedSizeTextView = findViewById(R.id.selectedSizeTextView)

        // Initialize Spinners
        initSpinners()
    }

    private var selectedWhiskyPricePerCl: Int = 0

    private fun initSpinners() {

        // Explicitly specify the adapter to use String as its data type
        whiskySpinner = findViewById<Spinner>(R.id.whiskySpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, whiskies).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedWhisky = whiskies[position]
                    selectedWhiskyPricePerCl = pricesPerCl[position]
                    selectedWhiskyTextView.text = if (position > 0) {
                        "$selectedWhisky - Price: $selectedWhiskyPricePerCl kr/cl"
                    } else {
                        getString(R.string.select_whisky_prompt)
                    }
                    updateWhiskyImage(selectedWhisky)
                    // Update the total sum TextView if a size is already selected
                    if (sizeSpinner.selectedItemPosition > 0) {
                        selectedSizeTextView.text = "Your choice is ${sizes[sizeSpinner.selectedItemPosition]}\n" +
                                "Total sum is ${sizes[sizeSpinner.selectedItemPosition].filter { it.isDigit() }.toInt() * selectedWhiskyPricePerCl} kr"
                    }
                }

            override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedWhiskyTextView.text = getString(R.string.select_whisky_prompt)
                }
            }
        }

        // Size Spinner
        sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, sizes).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val sizeSelection = sizes[position]
                    selectedSizeTextView.text = if (position > 0) {
                        val sizeInCl = sizeSelection.filter { it.isDigit() }.toInt() // Extracts numeric value
                        val totalPrice = sizeInCl * selectedWhiskyPricePerCl
                        "Your choice is $sizeInCl cl\nTotal sum is $totalPrice kr"
                    } else {
                        ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedSizeTextView.text = ""
                }
            }
        }

        // Artist Spinner
        artistSpinner = findViewById<Spinner>(R.id.artistSpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, artists).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedArtist = artists[position]
                    updateArtistImage(selectedArtist)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Logic for no selection, if needed
                }
            }
        }
    }

    private fun updateWhiskyImage(whiskyName: String) {
        val whiskyResId = getDrawableResourceByName(whiskyName)
        whiskyImage.setImageResource(whiskyResId)
    }

    private fun updateArtistImage(artistName: String) {
        val artistResId = getDrawableResourceByName(artistName)
        artistImage.setImageResource(artistResId)
    }

    private fun getDrawableResourceByName(name: String): Int {
        val resourceName = name.lowercase().replace(" ", "_").replace("'", "")
        return resources.getIdentifier(resourceName, "drawable", packageName)
    }
}