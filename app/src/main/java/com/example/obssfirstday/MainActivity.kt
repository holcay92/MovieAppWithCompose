package com.example.obssfirstday

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.obssfirstday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var randomNumber = (0..100).random()
    private var counter = 5


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("RandomNumber", "Random number is $randomNumber")
        binding.btnSubmit.setOnClickListener {

            val input = binding.et1.text.toString().toIntOrNull()
            if (input == null) {
                Toast.makeText(this, getString(R.string.empty_field_warning), Toast.LENGTH_SHORT)
                    .show()
            } else if (counter == 0) {
                binding.resultTextView.text = getString(R.string.message_game_over)
                binding.et1.text?.clear()
                binding.btnSubmit.isEnabled = false
                showResultDialog()
            } else {
                counter--
                if (input > randomNumber) {
                    binding.resultTextView.text = getString(R.string.message_decrease_your_number)
                    binding.et1.text?.clear()
                    binding.remainingAttemptsCount.text = counter.toString()
                } else if (input < randomNumber) {
                    binding.resultTextView.text = getString(R.string.message_increase_your_number)
                    binding.et1.text?.clear()
                    binding.remainingAttemptsCount.text = counter.toString()
                } else {
                    binding.resultTextView.text =
                        getString(R.string.message_congratulations) + " $randomNumber"
                    binding.btnSubmit.isEnabled = false
                    binding.btnTryAgain.visibility = android.view.View.VISIBLE
                }
            }
            if(counter == 0){
                binding.btnSubmit.isEnabled = false
                binding.btnTryAgain.visibility = android.view.View.VISIBLE
                showResultDialog()
            }
        }

        binding.btnTryAgain.setOnClickListener {
            randomNumber = (0..100).random()
            Log.d("RandomNumber", "New Random number is $randomNumber")
            counter = 5
            binding.remainingAttemptsCount.text = counter.toString()
            binding.resultTextView.text = getString(R.string.message_new_game_started)
            binding.et1.text?.clear()
            binding.btnTryAgain.visibility = android.view.View.GONE
            binding.btnSubmit.isEnabled = true
        }
    }

    //alert dialog function
    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Exit")
        alert.show()
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    private fun showResultDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("The number was $randomNumber. Do you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
                binding.btnTryAgain.visibility = android.view.View.VISIBLE
            }
        val alert = dialogBuilder.create()
        alert.setTitle("OBSS First Day")
        alert.show()
    }
}