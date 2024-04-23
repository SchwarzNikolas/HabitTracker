package com.example.habittracker.habit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.time.LocalDate

// modifies the state of the HabitState

//TODO update completion on edti
class HabitViewModel (
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HabitState())
    val state = _state

    init {

          Log.d ("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", LocalDate.now().dayOfWeek.value.toString())


        viewModelScope.launch {
            // sync data base and state
            // will clean up later
            dao.insertHabit(Habit(name = "test1", frequency = 1))
            dao.insertCompletion(HabitCompletion())
            dao.insertHabit(Habit(name = "test2", frequency = 2, occurrence = "1011111"))
            dao.insertCompletion(HabitCompletion())
            dao.insertHabit(Habit(name = "test3", frequency = 3, occurrence = "0100000"))
            dao.insertCompletion(HabitCompletion())
        }

//        viewModelScope.launch {
//            // get date from percistent storage
//            viewModelScope.launch {
//                while (true) {
//                    if (state.value.date != LocalDateTime.now()) {
//                        state.update { it.copy(date = LocalDateTime.now()) }
//                        resetCompletion()
//                        if (state.value.date.dayOfWeek.toString() == "MONDAY") {
//                            // reset weekly habit
//                        }
//                    }
//                    delay(2000)
//                }
//            }
//        }



        // On initiation
//        viewModelScope.launch {
//
//
//            // sync data base and state
//            // will clean up later
//             dao.insertHabit(Habit(name = "test2", frequency = 3))
//             dao.insertCompletion(HabitCompletion())
//
//            dao.fetchHabits().collect { habits ->
//                run {
//                    val displayHabitList: MutableList<DisplayHabit> = mutableListOf()
//                    for (habit in habits) {
//                        var wasFound = false
//                        for (displayHabit in state.value.habits) {
//                            if (habit in displayHabit) {
//                                if (habit.frequency != displayHabit.completion.size) {
//                                    val newCompletion: MutableList<MutableState<Boolean>> =
//                                        if (habit.frequency < displayHabit.completion.size) {
//                                            displayHabit.completion.subList(0, habit.frequency)
//                                        } else {
//                                            val n = habit.frequency - displayHabit.completion.size
//                                            displayHabit.completion.addAll(MutableList(size = n) {
//                                                mutableStateOf(
//                                                    displayHabit.done.value
//                                                )
//                                            })
//                                            displayHabit.completion
//                                        }
//                                    displayHabitList.add(
//                                        displayHabit.copy(
//                                            habit = mutableStateOf(
//                                                habit
//                                            ), completion = newCompletion
//                                        )
//                                    )
//                                } else {
//                                    displayHabitList.add(
//                                        displayHabit.copy(
//                                            habit = mutableStateOf(
//                                                habit
//                                            )
//                                        )
//                                    )
//                                }
//                                wasFound = true
//                                break
//                            }
//                        }
//                        if (wasFound.not()) {
//                            displayHabitList.add(DisplayHabit(habit = mutableStateOf(habit)))
//                        }
//                    }
//                    _state.update { it.copy(habits = displayHabitList) }
//                }
//            }
//        }
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
        val test: String = "_______"
        val sb = StringBuilder(test)
        //sb.setCharAt(LocalDate.now().dayOfWeek.value-1, '1').toString()
        viewModelScope.launch {
            dao.fetchHabitByDay().collect{habitJoin -> run{
                val displayHabitRecordList: MutableList<DisplayHabit> = mutableListOf()
                for (habit in habitJoin){
                    displayHabitRecordList.add(DisplayHabit(habitJoin = habit))
                }
                _state.update { it.copy(
                    displayHabits = displayHabitRecordList
                )
                }
            }
            }
        }

//        viewModelScope.launch {
//            dao.getHabit().collect{habitJoin -> run{
//                val displayHabitRecordList: MutableList<DisplayHabit> = mutableListOf()
//                for (habit in habitJoin){
//                    displayHabitRecordList.add(DisplayHabit(habitJoin = habit))
//                }
//                _state.update { it.copy(
//                    displayHabits = displayHabitRecordList
//                )
//                }
//            }
//            }
//        }
    }

    // Handles the events triggered by the UI
    fun onEvent(event: HabitEvent) {
        when (event) {
            // handles ModifyHabit event
            is HabitEvent.ModifyHabit -> {
                state.update {
                    it.copy(
                        showEdit = false
                    )
                }
                if (state.value.editFreq < state.value.edithabitJoin.completion.completion){
                    updateCompletion(state.value.edithabitJoin.completion.copy(completion = state.value.editFreq))
                }
                updateHabit(state.value.edithabitJoin.habit.copy(
                        frequency = state.value.editFreq,
                        name = state.value.editString
                    )
                )
            }

            is HabitEvent.EditHabit -> {
                event.displayHabit.beingEdited.value = event.displayHabit.beingEdited.value.not()
                state.update {
                    it.copy(
                        showEdit = true,
                        editString = event.displayHabit.habitJoin.habit.name,
                        editFreq = event.displayHabit.habitJoin.habit.frequency,
                        edithabitJoin = event.displayHabit.habitJoin
                    )
                }
            }

            // Deprecated
            is HabitEvent.BoxChecked -> {
                //if box is checked, it becomes unchecked and vice versa
                event.displayHabit.completion[event.index].value =
                    event.displayHabit.completion[event.index].value.not()
                // accesses if habit is completed
                //checkHabitCompletion(event.displayHabit)
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
                state.update {
                    it.copy(
                        showEdit = false
                    )
                }
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
                        val habtiCompletion = event.habitJoin.completion.copy(completion = event.habitJoin.completion.completion.inc())
                        checkHabitCompletion(event.habitJoin, habtiCompletion)
                    }
                }
            }

            is HabitEvent.DecCompletion -> {
                if(event.habitJoin.completion.completion > 0){
                    viewModelScope.launch {
                        val habitCompletion = event.habitJoin.completion.copy(completion = event.habitJoin.completion.completion.dec())
                        checkHabitCompletion(event.habitJoin, habitCompletion)
                    }
                }
            }

            // Deprecated
            HabitEvent.resetCompletion -> {
                viewModelScope.launch {
                    resetCompletion()
                }
            }
        }
    }

    // logic for habit completion, components are explained individually below
    private fun checkHabitCompletion(join: HabitJoin, completion: HabitCompletion) {
        val habitRecord = HabitRecord(
            habitName = join.habit.name,
            date = LocalDate.now().toString()
        )
        var comp: HabitCompletion = completion
        if (join.habit.frequency == completion.completion && !completion.done){
            comp = completion.copy(done = true)
            insertRecord(habitRecord)
        }else if(completion.done){
            comp = completion.copy(done = false)
            removeRecord(habitRecord)
        }
        updateCompletion(comp)
        // if all the the boxes are ticked AND the habit is not marked as completed,
        // the habit is marked as completed and a record is inserted into the database.
        // else if, the habit is marked as completed,
        // the habit is marked as uncompleted and the record of completion is remove from the database.
//        if (displayHabit.completion.stream()
//                .map { x -> x.value }
//                .allMatch { x -> x == true }
//            && !displayHabit.done.value
//        ) {
//            displayHabit.done.value = displayHabit.done.value.not()
//            insertRecord(habitRecord)
//
//        } else if (displayHabit.done.value) {
//            displayHabit.done.value = displayHabit.done.value.not()
//            removeRecord(habitRecord)
//        }
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
    // insert habit into the database using the dao (database access object)
    private fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            dao.insertHabit(habit)
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

    private suspend fun resetCompletion(){
        dao.resetCompletion()
    }
}

