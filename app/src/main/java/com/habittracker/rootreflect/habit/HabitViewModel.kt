package com.habittracker.rootreflect.habit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.rootreflect.database.DateRecord
import com.habittracker.rootreflect.database.Habit
import com.habittracker.rootreflect.database.HabitDao
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
    private lateinit var date: LocalDate
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
            val mood  = dao.getMoodRecByDate(date.toString())
            if (mood != null){
                state.update { it.copy(selectedMood = mood.mood) }
            }
            job = viewModelScope.launch { dataSupRoutine() }
            while (true) {
                if (LocalDate.now() != date) {
                    date = LocalDate.now()
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

                val habit: Habit = if (state.value.editFreq <= event.displayHabit.habit.completion){
                    event.displayHabit.habit.copy(
                        frequency = state.value.editFreq,
                        name = state.value.editString,
                        completion = state.value.editFreq,
                        occurrence = state.value.editDays,
                        done = false
                    )
                }else{
                    event.displayHabit.habit.copy(
                        frequency = state.value.editFreq,
                        name = state.value.editString,
                        occurrence = state.value.editDays
                    )
                }
                modifyHabit(event, habit)
            }

            is HabitEvent.EditHabit -> {
                state.value.editDisplayHabit.beingEdited.value = false
                event.displayHabit.beingEdited.value = event.displayHabit.beingEdited.value.not()
                state.update {
                    it.copy(
                        editString = event.displayHabit.habit.name,
                        editFreq = event.displayHabit.habit.frequency,
                        editDays = event.displayHabit.habit.occurrence,
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
                removeHabit(event.habit)
            }

            is HabitEvent.ContextMenuVisibility -> {
                event.displayHabit.isMenuVisible.value = event.displayHabit.isMenuVisible.value.not()
            }

            is HabitEvent.IncCompletion -> {
                if (event.habit.completion < event.habit.frequency) {
                    viewModelScope.launch {
                        val habit = event.habit.copy(completion = event.habit.completion.inc())
                        updateHabit(habit)
                    }
                }
            }

            is HabitEvent.DecCompletion -> {
                if(event.habit.completion > 0){
                    viewModelScope.launch {
                        val habit = event.habit.copy(completion = event.habit.completion.dec())
                        updateHabit(habit)
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
                val moodRecord =  MoodRecord(
                    //moodDate = dateTest,
                    moodDate = date,
                    mood = event.moodType
                )
                //val dateTest = LocalDate.now()
                viewModelScope.launch {
                        dao.upsertMoodRec(moodRecord)
                }
            }

            is HabitEvent.CheckCompletion -> {
                checkHabitCompletion(event.habit)
            }

            HabitEvent.ToggleNotificationVisibility -> {
                viewModelScope.launch {
                delay(2000)
                state.update { it.copy(showNotification = false) }
            }
            }

            HabitEvent.ToggleScroll -> {
                state.update {
                    it.copy(fixScroll = mutableStateOf(false)) }
            }
        }
    }



    // logic for habit completion, components are explained individually below
    private fun checkHabitCompletion(habit: Habit) {
        val habitRecord = HabitRecord(
            habitName = habit.name,
            habitFrequency = habit.frequency,
            date = date
        )

        if (habit.frequency == habit.completion){
            val  comp = habit.copy(done = true)
            insertRecord(habitRecord)
            updateHabit(comp)
        }else if(habit.done){
            val comp = habit.copy(done = false)
            removeRecord(habitRecord)
            updateHabit(comp)
        }
    }

    // removes record from the database using the dao (database access object)
    private fun removeHabit(habit: Habit){
        viewModelScope.launch {
            //dao.deleteCompletion(habitJoin.completion)
            dao.deleteHabit(habit)
        }
    }

//    private fun updateCompletion(completion: HabitCompletion){
//        viewModelScope.launch {
//            dao.upsertCompletion(completion)
//        }
//    }

    private fun removeRecord(record: HabitRecord) {
        viewModelScope.launch {
            dao.deleteRecord(record.habitName, record.date)
        }
    }

    // insert record into the database using the dao (database access object)
    private fun insertRecord(record: HabitRecord) {
        viewModelScope.launch {
            dao.upsertRecord(record)
        }
    }

    private fun updateHabit(habit: Habit){
        viewModelScope.launch {
            try {
                dao.upsertHabit(habit)
            } catch (e: Exception) {
                return@launch
            }
        }
    }

     private suspend fun dataSupRoutine(){
         dao.fetchHabitByDay(dayToString(date.dayOfWeek.value-1)).collect{habits -> run{

             val displayHabitRecordList: MutableList<DisplayHabit> = mutableListOf()

             for (habit in habits){
                 displayHabitRecordList.add(DisplayHabit(habit = habit))
             }
                _state.update { it.copy(
                    displayHabits = displayHabitRecordList,
                    fixScroll = mutableStateOf(true)
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

    private fun modifyHabit(event: HabitEvent.ModifyHabit, habit: Habit) {

        viewModelScope.launch {
            try {
                dao.upsertHabit(habit)
                if (state.value.editString != event.displayHabit.habit.name) {
                    removeRecord(HabitRecord(event.displayHabit.habit.name, event.displayHabit.habit.frequency, date))
                    insertRecord(HabitRecord(state.value.editString, state.value.editFreq, date))
                }
                checkHabitCompletion(habit)
            }catch (e: Exception){
                state.update { it.copy(editString = "Error: Name Already exists",
                    showNotification = true) }
                return@launch
            }finally {
                event.displayHabit.beingEdited.value = false
            }
        }
    }
}

