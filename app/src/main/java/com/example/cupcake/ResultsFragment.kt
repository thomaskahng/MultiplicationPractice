package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentResultsBinding
import com.example.cupcake.multiplication.MultiplicationFunctions
class ResultsFragment : Fragment() {
    //Data binding with this class and UI
    private var binding: FragmentResultsBinding? = null

    //"activityViewModels()" property delegation to call methods
    private val multView: MultiplicationFunctions by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentResultsBinding.inflate(inflater, container, false)
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
            resultsFragment = this@ResultsFragment
        }
    }

    fun sendResults() {
        //Stores a string of practice information
        val orderSummary = getString(
            R.string.mult_details,
            multView.name.value.toString(),
            multView.date.value.toString(),
            multView.numPrac.value.toString(),
            multView.percCorrect.value.toString()
        )
        //Implicit intent of type plain text with intent extras for email subject
        //and email body extra text for order summary
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_practice))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        //Prevent the app from crashing if no app to handle intent
        if (activity?.packageManager?.resolveActivity(intent, 0) != null)
            startActivity(intent)
    }

    //Destroy values and reset data binding
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //Cancel by resetting the game view and navigating back to start
    fun quit() {
        multView.reset()
        findNavController().navigate(R.id.action_resultsFragment_to_startFragment)
    }
}