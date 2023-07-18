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
                binding.apply {
                    resultTextView.text = getString(R.string.message_game_over)
                    et1.text?.clear()
                    btnSubmit.isEnabled = false
                }

                showResultDialog()
            } else {
                counter--
                if (input > randomNumber) {
                    binding.apply {
                        resultTextView.text = getString(R.string.message_decrease_your_number)
                        et1.text?.clear()
                        remainingAttemptsCount.text = counter.toString()
                    }

                } else if (input < randomNumber) {
                    binding.apply {
                        resultTextView.text = getString(R.string.message_increase_your_number)
                        et1.text?.clear()
                        remainingAttemptsCount.text = counter.toString()
                    }

                } else {
                    binding.apply {
                        resultTextView.text =
                            getString(R.string.message_congratulations) + " $randomNumber"
                        btnSubmit.isEnabled = false
                        btnTryAgain.visibility = android.view.View.VISIBLE
                    }

                }
            }
            if (counter == 0) {
                binding.apply {
                    btnSubmit.isEnabled = false
                    btnTryAgain.visibility = android.view.View.VISIBLE
                    resultTextView.text = getString(R.string.message_game_over)
                }
                showResultDialog()
            }
        }

        binding.btnTryAgain.setOnClickListener {
            randomNumber = (0..100).random()
            Log.d("RandomNumber", "New Random number is $randomNumber")
            counter = 5
            binding.apply {
                remainingAttemptsCount.text = counter.toString()
                resultTextView.text = getString(R.string.message_new_game_started)
                et1.text?.clear()
                btnTryAgain.visibility = android.view.View.GONE
                btnSubmit.isEnabled = true
            }
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
        dialogBuilder.setMessage("The number was $randomNumber.")
            //.setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                //finish()
                dialog.cancel()
            }
            /*.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
                binding.btnTryAgain.visibility = android.view.View.VISIBLE
            }*/
        val alert = dialogBuilder.create()
        alert.setTitle("OBSS First Day")
        alert.show()
    }
}