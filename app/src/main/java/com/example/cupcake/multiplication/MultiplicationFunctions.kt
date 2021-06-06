package com.example.cupcake.multiplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class MultiplicationFunctions: ViewModel() {
    //Mutable data to calculate score from 0, then live data to return on screen
    private var _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    //Mutable data number of problems finished, then live data to return on screen
    private var _currProbCount = MutableLiveData(0)
    val currProbCount: LiveData<Int>
        get() = _currProbCount

    //Mutable data current problem
    private var _problem = MutableLiveData<String>()
    val problem: LiveData<String>
        get() = _problem

    //Current number of multiplication
    private val _num = MutableLiveData(0)
    val num: LiveData<Int> = _num

    //Number to multiply current number by
    private val _fact = MutableLiveData(0)

    //Answer of our problem
    private val _answer = MutableLiveData(0)
    val answer: LiveData<Int> = _answer

    //Correct or not
    private val _attempts = MutableLiveData(0)
    val attempts: LiveData<Int> = _attempts

    //Number correct
    private val _correct = MutableLiveData(0)
    val correct: LiveData<Int> = _correct

    //Name of user
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    //Today's date
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date
    //For today's date
    val dateOptions = getToday()

    //Number practiced
    private val _numPrac = MutableLiveData<String>()
    val numPrac: LiveData<String> = _numPrac
    //Percent correct
    private val _percCorrect = MutableLiveData<String>()
    val percCorrect: LiveData<String> = _percCorrect

    //Constructor sets our number
    init {
        getNextProb()
    }

    //Get next problem
    private fun getNextProb() {
        //Increase factor and show next problem
        _fact.value = (_fact.value)?.inc()
        _problem.value = (_num.value).toString() + " x " + (_fact.value).toString()

        //One more problem counted
        _currProbCount.value = (_currProbCount.value)?.inc()
        //Set the answer
        setAnswer(_num.value, _fact.value)
    }

    //Reset data for next play
    fun reset() {
        _score.value = 0
        _currProbCount.value = 0
        _problem.value = ""
        _fact.value = 0

        _attempts.value = 0
        _correct.value = 0

        _name.value = ""
        _date.value = dateOptions[0]

        _numPrac.value = ""
        _percCorrect.value = ""
        getNextProb()
    }

    //Number to practice
    fun pracNumber(number: Int) {
        _num.value = number

        //Set the problem displayed and answer
        _problem.value = (_num.value).toString() + " x " + (_fact.value).toString()
        setAnswer(_num.value, _fact.value)
    }

    //Did user get problem right
    fun isCorrect(guess: Int?): Boolean {
        //If guess correct, true, else false
        if (guess == _answer.value) {

            //First try correct, add points and one more correct
            if (_attempts.value == 0) {
                _score.value = (_score.value)?.plus(10)
                _correct.value = (_correct.value)?.plus(1)
            }
            //Reset value of attempts, and show correct
            _attempts.value = 0
            return true
        }
        //Wrong, so one attempt used
        else {
            _attempts.value = (_attempts.value)?.plus(1)
            return false
        }
    }

    //Blank or invalid submissions are wrong!
    fun inputBlankOrInvalid(input: String) {
        _attempts.value = (_attempts.value)?.plus(1)
    }

    //Get next problem & true if less than 12 problems, else false
    fun nextProb(): Boolean {
        return if (_currProbCount.value!! < 12) {
            getNextProb()
            true
        } else false
    }

    //Set answer of a problem
    private fun setAnswer(num: Int?, fact: Int?) {
        if (fact != null)
            _answer.value = num!! * fact
    }

    fun getToday(): List<String> {
        //Mutable string list of pickup options
        val options = mutableListOf<String>()

        //Format a date, next line get a calendar date instance
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        //Add the current date on the calendar to options
        options.add(formatter.format(calendar.time))

        //Set date and return
        _date.value = options[0]
        return options
    }

    //What did we practice?
    fun decNumPrac() {
        _numPrac.value = _num.value.toString() + "s"
    }

    //Fraction of correct
    fun decPercCorr() {
        _percCorrect.value = _correct.value.toString() + " / " + 12
    }

    //Set user's name
    fun userName(name: String) {
        _name.value = name
    }

    //Was name entered?
    fun isNameEntered(): Boolean {
        if (!_name.value.isNullOrEmpty())
            return true
        else
            return false
    }
}
