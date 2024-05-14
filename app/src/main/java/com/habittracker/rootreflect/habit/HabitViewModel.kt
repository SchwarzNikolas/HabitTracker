package com.habittracker.rootreflect.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.DateRecord
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitCompletion
import com.habittracker.rootreflect.database.HabitDao
import com.habittracker.rootreflect.database.HabitJoin
import com.habittracker.rootreflect.database.HabitRecord
import com.habittracker.rootreflect.database.MoodRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

// modifies the state of the HabitState

private fun Boolean.toChar(): Char {
    return if(this) '1' else '0'
}

class HabitViewModel (
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HabitState())
    val state = _state
    private lateinit var date:LocalDate
    private lateinit var job: Job
    private val maxHabitNameLength = 50
    private val maxHabitFrequency = 9
    init {
        viewModelScope.launch {

            val dateRecord = dao.getDate()
            if (dateRecord == null){
                dao.upsertDate(DateRecord(key = 1, date = LocalDate.now()))
                date = LocalDate.now()
            }else{
               date = dateRecord.date
            }

            job = viewModelScope.launch { dataSupRoutine() }
            while (true) {
                if (getDate() != date) {
                    date = getDate()
                    dao.upsertDate(DateRecord(key = 1, date = LocalDate.now()))
                    resetCompletion()
                    job.cancel()
                    job = viewModelScope.launch { dataSupRoutine() }
                }
                delay(500)
            }
        }


        viewModelScope.launch {
////             sync data base and state
////             will clean up later
//            dao.insertHabit(Habit(name = "test1", frequency = 5))
//            dao.insertCompletion(HabitCompletion())
//            dao.insertHabit(Habit(name = "test1", frequency = 3,))
//            dao.insertCompletion(HabitCompletion(occurrence = "1000000"))
//            dao.insertHabit(Habit(name = "test2", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0100000"))
//            dao.insertHabit(Habit(name = "test3", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0010000"))
//            dao.insertHabit(Habit(name = "test4", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0001000"))
//            dao.insertHabit(Habit(name = "test5", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0000100"))
//            dao.insertHabit(Habit(name = "test6", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0000010"))
//            dao.insertHabit(Habit(name = "test7", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0000001"))
        }
    }

    // Handles the events triggered by the UI
    fun onEvent(event: HabitEvent) {
        when (event) {
            // handles ModifyHabit event
            is HabitEvent.ModifyHabit -> {
                val habitCompletion: HabitCompletion = if (state.value.editFreq <= event.joinHabit.completion.completion){
                    event.joinHabit.completion.copy(completion = state.value.editFreq,occurrence = state.value.editDays)
                }else{
                    event.joinHabit.completion.copy(occurrence = state.value.editDays)
                }
                val habit = event.joinHabit.habit.copy(
                    frequency = state.value.editFreq,
                    name = state.value.editString
                )

                updateCompletion(habitCompletion)
                checkHabitCompletion(habit, habitCompletion)
                updateHabit(habit)
            }

            is HabitEvent.EditHabit -> {
                state.value.editDisplayHabit.beingEdited.value = false
                event.displayHabit.beingEdited.value = event.displayHabit.beingEdited.value.not()
                state.update {
                    it.copy(
                        editString = event.displayHabit.habitJoin.habit.name,
                        editFreq = event.displayHabit.habitJoin.habit.frequency,
                        editDays = event.displayHabit.habitJoin.completion.occurrence,
                        editDisplayHabit = event.displayHabit
                    )
                }
            }
            // when text is entered by the user into the text field is updated here and temporarily stored in the habit state.
            is HabitEvent.UpDateEditFreq -> {
                if(event.newFreq <=  maxHabitFrequency) {
                    state.update {
                        it.copy(
                            editFreq = event.newFreq
                        )
                    }
                }
            }

            // when text is entered by the user into the text field is updated here and temporarily stored in the habit state.
            is HabitEvent.UpDateEditString -> {
                if (event.newString.length <= maxHabitNameLength) {
                    state.update {
                        it.copy(
                            editString = event.newString
                        )
                    }
                }
            }

            // changes days of a weekly habit
            is HabitEvent.UpDateEditDays -> {
                val sb  = StringBuilder(state.value.editDays)
                sb.setCharAt(event.newDayIndex, event.clicked.not().toChar())
                state.update { it.copy(
                    editDays = sb.toString()
                ) }
            }

            // closes the edit window
            is HabitEvent.CancelEdit -> {
                event.displayHabit.beingEdited.value = false
            }

            // deletes habit from database (is also automatically removed from UI)(gone forever)
            is HabitEvent.DeleteHabit -> {
                removeHabit(event.habitJoin)
            }

            is HabitEvent.ContextMenuVisibility -> {
                event.displayHabit.isMenuVisible.value = event.displayHabit.isMenuVisible.value.not()
            }

            is HabitEvent.IncCompletion -> {
                if (event.habitJoin.completion.completion < event.habitJoin.habit.frequency) {
                    viewModelScope.launch {
                        val habitCompletion = event.habitJoin.completion.copy(completion = event.habitJoin.completion.completion.inc())
                        checkHabitCompletion(event.habitJoin.habit, habitCompletion)
                    }
                }
            }

            is HabitEvent.DecCompletion -> {
                if(event.habitJoin.completion.completion > 0){
                    viewModelScope.launch {
                        val habitCompletion = event.habitJoin.completion.copy(completion = event.habitJoin.completion.completion.dec())
                        checkHabitCompletion(event.habitJoin.habit, habitCompletion)
                    }
                }
            }

            // Deprecated
            HabitEvent.ResetCompletion -> {
                resetCompletion()
            }

            /*is HabitEvent.MoodChange -> {
                _state.update {
                    it.copy(
                        selectedMood = event.moodType
                    )
                }
                viewModelScope.launch {
                    dao.upsertMoodRec(MoodRecord(date, event.moodType))
                }
            }*/

            is HabitEvent.MoodSelected -> {
                _state.update {
                    it.copy(
                        selectedMood = event.moodType
                    )
                }
                viewModelScope.launch {
                    val existingRec = dao.getMoodRecByDate(date.toString())
                    if (existingRec == null) {
                        dao.insertMoodRec(
                            moodRec = MoodRecord(
                                moodDate = date,
                                mood = event.moodType
                            )
                        )
                    }
                }
            }
        }
    }



    // logic for habit completion, components are explained individually below
    private fun checkHabitCompletion(join: Habit, completion: HabitCompletion) {
        val habitRecord = HabitRecord(
            habitName = join.name,
            habitFrequency = join.frequency,
            date = date

        )
        var comp: HabitCompletion = completion
        if (join.frequency == completion.completion && !completion.done){
            comp = completion.copy(done = true)
            insertRecord(habitRecord)
        }else if(completion.done && join.frequency != completion.completion){
            comp = completion.copy(done = false)
            removeRecord(habitRecord)
        }
        updateCompletion(comp)
    }

    // removes record from the database using the dao (database access object)
    private fun removeHabit(habitJoin: HabitJoin){
        viewModelScope.launch {
            //dao.deleteCompletion(habitJoin.completion)
            dao.deleteHabit(habitJoin.habit)
        }
    }

    private fun updateCompletion(completion: HabitCompletion){
        viewModelScope.launch {
            dao.upsertCompletion(completion)
        }
    }

    private fun removeRecord(record: HabitRecord) {
        viewModelScope.launch {
            dao.deleteRecord(record.habitName, record.date)
        }
    }

    // insert record into the database using the dao (database access object)
    private fun insertRecord(record: HabitRecord) {
        viewModelScope.launch {
            dao.insertRecord(record)
        }
    }

    private fun updateHabit(habit: Habit){
        viewModelScope.launch {
            dao.upsertHabit(habit)
        }
    }

     private suspend fun dataSupRoutine(){
         dao.fetchHabitByDay(dayToString(date.dayOfWeek.value-1)).collect{habitJoin -> run{
             val displayHabitRecordList: MutableList<DisplayHabit> = mutableListOf()
             val weeklyDisplayHabitRecordList: MutableList<DisplayHabit> = mutableListOf()
             for (habit in habitJoin){
                if (habit.completion.occurrence == "1111111"){
                    displayHabitRecordList.add(DisplayHabit(habitJoin = habit))
                }else{
                    weeklyDisplayHabitRecordList.add(DisplayHabit(habitJoin = habit))
                }
             }
                _state.update { it.copy(
                    displayHabits = displayHabitRecordList,
                    weeklyDisplayHabits = weeklyDisplayHabitRecordList
                )
                }
         }
         }
    }

    private fun dayToString(day : Int): String{
        val test = "_______"
        val sb = StringBuilder(test)
        sb.setCharAt(day, '1')
        return sb.toString()
    }


    private fun resetCompletion(){
        viewModelScope.launch {
            dao.resetCompletion()
        }
    }

    // tem
    private fun getDate(): LocalDate {
        return LocalDate.now()
    }

}

