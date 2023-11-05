package com.example.gavle_uppgift_02_whiskey

// Required imports for Android UI components and AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Main Activity class of the Android application
class MainActivity : AppCompatActivity() {

    // Lateinit properties for the UI components, to be initialized later
    private lateinit var whiskyImage: ImageView
    private lateinit var artistImage: ImageView
    private lateinit var selectedWhiskyTextView: TextView
    private lateinit var selectedSizeTextView: TextView

    private lateinit var whiskySpinner: Spinner
    private lateinit var sizeSpinner: Spinner
    private lateinit var artistSpinner: Spinner

    // Lazy initialization of the string and integer arrays from the resources
    private val whiskies by lazy { resources.getStringArray(R.array.whiskies) }
    private val sizes by lazy { resources.getStringArray(R.array.sizes) }
    private val artists by lazy { resources.getStringArray(R.array.artists) }
    private val pricesPerCl by lazy { resources.getIntArray(R.array.prices) }

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connecting the variables to the UI components by their IDs
        whiskyImage = findViewById(R.id.whiskyImage)
        artistImage = findViewById(R.id.artistImage)
        selectedWhiskyTextView = findViewById(R.id.selectedWhiskyTextView)
        selectedSizeTextView = findViewById(R.id.selectedSizeTextView)

        // Initialize the Spinners (dropdowns) with their respective adapters and listeners
        initSpinners()
    }

    // Placeholder for the selected whisky price per cl
    private var selectedWhiskyPricePerCl: Int = 0

    // Function to initialize all spinners with adapters and define their item selected listeners
    private fun initSpinners() {

        // Whisky Spinner setup
        whiskySpinner = findViewById<Spinner>(R.id.whiskySpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, whiskies).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedWhisky = whiskies[position]
                    selectedWhiskyPricePerCl = pricesPerCl[position]
                    // Set the text view to show the selected whisky and its price, or prompt if none selected
                    selectedWhiskyTextView.text = if (position > 0) {
                        "Your choice is $selectedWhisky - Price: $selectedWhiskyPricePerCl kr/cl"
                    } else {
                        getString(R.string.select_whisky_prompt)
                    }
                    // Update the whisky image based on the selection
                    updateWhiskyImage(selectedWhisky)
                    // Update the total sum TextView if a size has already been selected
                    if (sizeSpinner.selectedItemPosition > 0) {
                        val sizeInCl = sizes[sizeSpinner.selectedItemPosition].filter { it.isDigit() }.toInt() // Extract numeric part
                        val totalPrice = sizeInCl * selectedWhiskyPricePerCl
                        selectedSizeTextView.text = "Your choice is $sizeInCl cl\nTotal sum is $totalPrice kr"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedWhiskyTextView.text = getString(R.string.select_whisky_prompt)
                }
            }
        }

        // Size Spinner setup
        sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, sizes).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val sizeSelection = sizes[position]
                    // Set the text view to show the selected size and calculate the total price
                    selectedSizeTextView.text = if (position > 0) {
                        val sizeInCl = sizeSelection.filter { it.isDigit() }.toInt() // Extract numeric part
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

        // Artist Spinner setup
        artistSpinner = findViewById<Spinner>(R.id.artistSpinner).apply {
            adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, artists).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedArtist = artists[position]
                    // Update the artist image based on the selection
                    updateArtistImage(selectedArtist)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // This can be left empty if no action is needed for no selection case
                }
            }
        }
    }

    // Function to update the whisky image based on the name
    private fun updateWhiskyImage(whiskyName: String) {
        val whiskyResId = getDrawableResourceByName(whiskyName)
        whiskyImage.setImageResource(whiskyResId)
    }

    // Function to update the artist image based on the name
    private fun updateArtistImage(artistName: String) {
        val artistResId = getDrawableResourceByName(artistName)
        artistImage.setImageResource(artistResId)
    }

    // Helper function to get the drawable resource ID by its name
    private fun getDrawableResourceByName(name: String): Int {
        val resourceName = name.lowercase().replace(" ", "_").replace("'", "")
        return resources.getIdentifier(resourceName, "drawable", packageName)
    }
}