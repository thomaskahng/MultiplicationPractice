package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentMultiplicationBinding
import com.example.cupcake.multiplication.MultiplicationFunctions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Integer.parseInt

class MultiplicationFragment: Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentMultiplicationBinding
    //"activityViewModels()" property delegation to call methods
    private val multView: MultiplicationFunctions by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMultiplicationBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //Sets software life cycle owner
            lifecycleOwner = viewLifecycleOwner

            //Bind view model with shared model inside layout
            multViewModel = multView

            //Binds data variable with fragment instance (XML data variable calls functions)
            multiplicationFragment = this@MultiplicationFragment
        }
        //Update the UI
        updateNextProb()
    }

    fun onSubmitProb() {
        //Evaluate user input and convert to answer of problem
        val input = binding.textInputEditText.text.toString()
        val answer = inputToNum(input)
        val intString = isIntString(input)

        //Test if input was valid
        if (input.isNotBlank() && intString == true) {
            //Correct, no error and continue
            if (multView.isCorrect(answer)) {
                setErrorField(false)

                //If no next problem, show final score in dialog (modified)
                if (!multView.nextProb())
                    showFinalScore()
            }
            //Not correct, show error
            else {
                setErrorField(true)

                //Show answer if more than 3 incorrect attempts
                if (multView.attempts.value!! >= 3)
                    showAnswer()
            }
        }
        //If input was blank or invalid
        else {
            multView.inputBlankOrInvalid(input)
            setErrorField(true)

            //Show answer if more tham 3 incorrect attempts
            if (multView.attempts.value!! >= 3)
                showAnswer()
        }
    }

    private fun inputToNum(input: String): Int {
        var num = 0

        //Is user input string a number?
        try {
            num = parseInt(input)
        }
        catch(e: NumberFormatException) {
            num = 0
        }
        return num
    }

    private fun isIntString(input: String): Boolean {
        var isInt = true

        //Is user input string a number?
        try {
            var num = parseInt(input)
        }
        catch(e: NumberFormatException) {
            isInt = false
        }
        //Can be converted to int, then true, else false
        if (isInt == true)
            return true
        else
            return false
    }

    //Show answer after a number of incorrect attempts
    private fun showAnswer() {
        //Show message, and the answer
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("The answer to " + multView.problem.value.toString() + " is:")
            .setMessage(multView.answer.value.toString())
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
            }
            .show()
    }

    private fun showFinalScore() {
        //Show message, score, and play again? in dialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, multView.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    //Reset game and show next problem
    private fun restartGame() {
        multView.reset()
        setErrorField(false)
        updateNextProb()
    }

    //Go to game results after enter name of user
    private fun exitGame() {
        multView.decNumPrac()
        multView.decPercCorr()
        findNavController().navigate(R.id.action_multiplicationFragment_to_nameFragment)
    }

    private fun setErrorField(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        }
        //Otherwise, no error message
        else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    //Update the problem in display
    private fun updateNextProb() {
        binding.textViewProb.text = multView.problem.value
    }
}