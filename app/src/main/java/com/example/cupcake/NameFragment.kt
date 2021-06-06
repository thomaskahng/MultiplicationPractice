package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentNameBinding
import com.example.cupcake.multiplication.MultiplicationFunctions

class NameFragment : Fragment() {
    //Data binding with this class and UI
    private lateinit var binding: FragmentNameBinding

    //"activityViewModels()" property delegation to call methods
    private val multView: MultiplicationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentNameBinding.inflate(inflater, container, false)
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
            nameFragment = this@NameFragment
        }
    }

    fun saveName() {
        //Evaluate user input and convert to answer of problem
        val name = binding.textInputEditText.text.toString()
        multView.userName(name)

        //Was a valid name entered?
        if (multView.isNameEntered()) {
            setErrorField(false)
            findNavController().navigate(R.id.action_nameFragment_to_resultsFragment)
        }
        //If name not entered
        else
            setErrorField(true)
    }

    private fun setErrorField(error: Boolean) {
        //Show error message if necessary
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.enter_valid_name)
        }
        //Otherwise, no error message
        else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
}