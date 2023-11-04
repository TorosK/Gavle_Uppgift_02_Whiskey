// MainActivity.kt

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
    private lateinit var priceText: TextView
    private lateinit var infoText: TextView

    private val whiskies by lazy { resources.getStringArray(R.array.whiskies) }
    private val prices by lazy { resources.getStringArray(R.array.prices) }
    private val artists by lazy { resources.getStringArray(R.array.artists) }
    private val sizes by lazy { resources.getStringArray(R.array.sizes) }

    private var selectedWhisky = ""
    private var selectedArtist = ""
    private var selectedSize = ""

    private lateinit var whiskySpinner: Spinner
    private lateinit var artistSpinner: Spinner
    private lateinit var sizeSpinner: Spinner

    private val pricesPerCl by lazy { resources.getIntArray(R.array.prices) }
    private val priceTextFormat by lazy { resources.getString(R.string.info_text_format) }
    private val sizeTextFormat by lazy { resources.getString(R.string.size_text_format) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisera ImageViews och TextViews
        whiskyImage = findViewById(R.id.whiskyImage)
        artistImage = findViewById(R.id.artistImage)
        priceText = findViewById(R.id.priceText)
        infoText = findViewById(R.id.infoText)

        // Initialisera Spinners
        whiskySpinner = findViewById(R.id.whiskySpinner)
        artistSpinner = findViewById(R.id.artistSpinner)
        sizeSpinner = findViewById(R.id.sizeSpinner)

        val whiskyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, whiskies)
        whiskyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        whiskySpinner.adapter = whiskyAdapter

        val artistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, artists)
        artistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        artistSpinner.adapter = artistAdapter

        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeSpinner.adapter = sizeAdapter

        whiskySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWhisky = whiskies[position]
                updateInfo()
            }
        }

        artistSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedArtist = artists[position]
                updateInfo()
            }
        }

        sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSize = sizes[position]
                updateInfo()
            }
        }
    }

    // UpdateInfo-funktionen definieras här för att tillåta åtkomst till ImageView och TextView.
    private fun updateInfo() {
        // Uppdaterar infoText med vald whisky, konstnär och storlek
        infoText.text = getString(R.string.info_text_format, selectedWhisky, selectedArtist, selectedSize)

        // Uppdaterar bilden för den valda whiskyn
        val whiskyResId = getWhiskyImageResource(selectedWhisky)
        whiskyImage.setImageResource(whiskyResId)

        // Uppdaterar bilden för den valda konstnären
        val artistResId = getArtistImageResource(selectedArtist)
        artistImage.setImageResource(artistResId)

        // Uppdaterar priset för den valda whiskyn
        // Find index of selected whisky
        val whiskyIndex = whiskies.indexOf(selectedWhisky).takeIf { it >= 0 } // Include the first item
        val sizeIndex = sizes.indexOf(selectedSize).takeIf { it >= 0 } // Include the first item

        // Update price text only if a whisky and a size are selected and not placeholders
        if (whiskyIndex != null && sizeIndex != null && selectedWhisky.isNotEmpty() && selectedSize.isNotEmpty()) {
            val pricePerCl = pricesPerCl[whiskyIndex]
            val sizeInCl = sizes[sizeIndex].filter { it.isDigit() }.toIntOrNull() ?: 0 // Safely convert to Int
            val totalPrice = pricePerCl * sizeInCl
            priceText.text = priceTextFormat.format(totalPrice.toFloat())

            // Update size and total price text
            infoText.text = sizeTextFormat.format(selectedSize, totalPrice.toFloat())
        }
    }

    private fun getWhiskyImageResource(whiskyName: String): Int {
        // Antag att du har en funktion som mappar whiskyName till en drawable-resurs.
        // Implementera den här logiken baserat på ditt specifika namngivningsschema.
        return resources.getIdentifier(whiskyName.lowercase().replace(" ", "_"), "drawable",
        packageName)
    }

    private fun getArtistImageResource(artistName: String): Int {
        // Samma som ovan, mappa artistName till en drawable-resurs.
        return resources.getIdentifier(artistName.lowercase().replace(" ", "_"), "drawable",
            packageName)
    }
}