package com.example.habittracker.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

// modifies the state of the HabitState

//TODO update completion on edit
class HabitViewModel (
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HabitState(job = Job()))
    val state = _state

    init {

        viewModelScope.launch {

            while (true) {
                if (getDate() != state.value.date) {
                    state.update {
                        it.copy(
                            date = getDate()
                        )
                    }
                    resetDailyCompletion()
                    state.value.job.cancel()

                    state.value.job = viewModelScope.launch { dataSupRoutine() }
                }
                delay(500)
            }
        }


        viewModelScope.launch {
////             sync data base and state
////             will clean up later
//            dao.insertHabit(Habit(name = "test1", frequency = 1))
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

        viewModelScope.launch {
            dao.fetchHabitRecords().collect{habitRecords -> run{
                val displayHabitRecordList: MutableList<HabitRecord> = mutableListOf()
                for (record in habitRecords){
                    displayHabitRecordList.add(record)
                }
                _state.update { it.copy(
                    habitRecord = displayHabitRecordList
                )
                }
            }
            }
        }
//
//        state.value.job = viewModelScope.launch {
//            dataSupRoutine()
//        }
    }

    // Handles the events triggered by the UI
    fun onEvent(event: HabitEvent) {
        when (event) {
            // handles ModifyHabit event
            is HabitEvent.ModifyHabit -> {
                val habitCompletion: HabitCompletion
                if (state.value.editFreq <= event.joinHabit.completion.completion){
                    habitCompletion = event.joinHabit.completion.copy(completion = state.value.editFreq)
                    updateCompletion(habitCompletion)

                }else{
                    habitCompletion = event.joinHabit.completion
                }
                val habit = event.joinHabit.habit.copy(
                    frequency = state.value.editFreq,
                    name = state.value.editString
                )

                checkHabitCompletion(habit, habitCompletion)
                updateHabit(habit)
            }

            is HabitEvent.EditHabit -> {
                event.displayHabit.beingEdited.value = event.displayHabit.beingEdited.value.not()
                state.update {
                    it.copy(
                        editString = event.displayHabit.habitJoin.habit.name,
                        editFreq = event.displayHabit.habitJoin.habit.frequency,
                        editDisplayHabit = event.displayHabit
                    )
                }
            }

            // Deprecated
            is HabitEvent.BoxChecked -> {
                //if box is checked, it becomes unchecked and vice versa
//                event.displayHabit.completion[event.index].value =
//                    event.displayHabit.completion[event.index].value.not()
//                // accesses if habit is completed
//                //checkHabitCompletion(event.displayHabit)
            }

            // when text is entered by the user into the text field is updated here and temporarily stored in the habit state.
            is HabitEvent.UpDateEditFreq -> {
                state.update {
                    it.copy(
                        editFreq = event.newFreq
                    )
                }
            }

            // when text is entered by the user into the text field is updated here and temporarily stored in the habit state.
            is HabitEvent.UpDateEditString -> {
                state.update {
                    it.copy(
                        editString = event.newString
                    )
                }
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
                resetDailyCompletion()
            }
            // Deprecated
            HabitEvent.NextDay -> {
                state.update { it.copy(date2 = state.value.date2.plusDays(1)) }
            }
        }
    }

    // logic for habit completion, components are explained individually below
    private fun checkHabitCompletion(join: Habit, completion: HabitCompletion) {
        val habitRecord = HabitRecord(
            habitName = join.name,
            date = LocalDate.now().toString()
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
            dao.deleteCompletion(habitJoin.completion)
            dao.deleteHabit(habitJoin.habit)
        }
    }

    private fun updateCompletion(completion: HabitCompletion){
        viewModelScope.launch {
            dao.updateCompletion(completion)
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
            dao.updateHabit(habit)
        }
    }

     private suspend fun dataSupRoutine(){
         dao.fetchHabitByDay(dayToString(state.value.date.dayOfWeek.value-1)).collect{habitJoin -> run{
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
        val test: String = "_______"
        val sb = StringBuilder(test)
        sb.setCharAt(day, '1')
        return sb.toString()
    }

    private fun resetDailyCompletion(){
        viewModelScope.launch {
            dao.resetDailyCompletion()
        }
    }

    private fun resetCompletion(){
        viewModelScope.launch {
            dao.resetCompletion()
        }
    }

    // tem

    private fun getDate(): LocalDateTime {
        return state.value.date2
    }

}

