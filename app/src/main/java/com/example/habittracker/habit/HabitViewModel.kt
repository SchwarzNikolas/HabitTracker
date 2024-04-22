package com.example.habittracker.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.concurrent.thread

// modifies the state of the HabitState
class HabitViewModel (
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HabitState())
    val state = _state

    init {

        // On initiation
        viewModelScope.launch {


            // sync data base and state
            // will clean up later
            //dao.insertHabit(Habit(name = "test2", frequency = 3))
            //dao.insertCompletion(HabitCompletion())

            dao.fetchHabits().collect { habits ->
                run {
                    val displayHabitList: MutableList<DisplayHabit> = mutableListOf()
                    for (habit in habits) {
                        var wasFound = false
                        for (displayHabit in state.value.habits) {
                            if (habit in displayHabit) {
                                if (habit.frequency != displayHabit.completion.size) {
                                    val newCompletion: MutableList<MutableState<Boolean>> =
                                        if (habit.frequency < displayHabit.completion.size) {
                                            displayHabit.completion.subList(0, habit.frequency)
                                        } else {
                                            val n = habit.frequency - displayHabit.completion.size
                                            displayHabit.completion.addAll(MutableList(size = n) {
                                                mutableStateOf(
                                                    displayHabit.done.value
                                                )
                                            })
                                            displayHabit.completion
                                        }
                                    displayHabitList.add(
                                        displayHabit.copy(
                                            habit = mutableStateOf(
                                                habit
                                            ), completion = newCompletion
                                        )
                                    )
                                } else {
                                    displayHabitList.add(
                                        displayHabit.copy(
                                            habit = mutableStateOf(
                                                habit
                                            )
                                        )
                                    )
                                }
                                wasFound = true
                                break
                            }
                        }
                        if (wasFound.not()) {
                            displayHabitList.add(DisplayHabit(habit = mutableStateOf(habit)))
                        }
                    }
                    _state.update { it.copy(habits = displayHabitList) }
                }
            }
        }
        viewModelScope.launch {
            dao.fetchHabitRecords().collect{habitRecords -> run{
                val displayHabitRecordList: MutableList<HabitRecord> = mutableListOf()
                for (habit in habitRecords){
                    displayHabitRecordList.add(habit)
                }
                _state.update { it.copy(
                    habitRecord = displayHabitRecordList
                )
                }
            }
            }
        }
        viewModelScope.launch {
            dao.getHabit().collect{habitJoin -> run{
                val displayHabitRecordList: MutableList<HabitJoin> = mutableListOf()
                for (habit in habitJoin){
                    displayHabitRecordList.add(habit)
                }
                _state.update { it.copy(
                    habitJoin = displayHabitRecordList
                )
                }
            }
            }
        }
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
                insertHabit(
                    state.value.editHabit.copy(
                        frequency = state.value.editFreq.toInt(),
                        name = state.value.editString
                    )
                )
            }

            is HabitEvent.EditHabit -> {
                state.update {
                    it.copy(
                        showEdit = true,
                        editString = event.displayHabit.value.name,
                        editFreq = event.displayHabit.value.frequency.toString(),
                        editHabit = event.displayHabit.value
                    )
                }
            }

            is HabitEvent.BoxChecked -> {
                //if box is checked, it becomes unchecked and vice versa
                event.displayHabit.completion[event.index].value =
                    event.displayHabit.completion[event.index].value.not()
                // accesses if habit is completed
                checkHabitCompletion(event.displayHabit)
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
                viewModelScope.launch {
                    dao.deleteHabit(event.displayHabit.habit.value)
                }
            }
            is HabitEvent.ContextMenuVisibility -> {
                event.displayHabit.isMenuVisible.value = event.displayHabit.isMenuVisible.value.not()
            }

            is HabitEvent.incCompletion -> {
                viewModelScope.launch {
                dao.updateCompletion(event.completion.copy(completion = event.completion.completion.inc()))
                }
            }

            is HabitEvent.decComletion -> {
                viewModelScope.launch {
                    dao.updateCompletion(event.completion.copy(completion = event.completion.completion.dec()))
                }
            }

            HabitEvent.resetCompletion -> {
                viewModelScope.launch {
                    resetCompletion()
                }
            }
        }
    }

    // logic for habit completion, components are explained individually below
    private fun checkHabitCompletion(displayHabit: DisplayHabit) {
        val habitRecord = HabitRecord(
            habitName = displayHabit.habit.value.name,
            date = LocalDate.now().toString()
        )

        // if all the the boxes are ticked AND the habit is not marked as completed,
        // the habit is marked as completed and a record is inserted into the database.
        // else if, the habit is marked as completed,
        // the habit is marked as uncompleted and the record of completion is remove from the database.
        if (displayHabit.completion.stream()
                .map { x -> x.value }
                .allMatch { x -> x == true }
            && !displayHabit.done.value
        ) {
            displayHabit.done.value = displayHabit.done.value.not()
            insertRecord(habitRecord)

        } else if (displayHabit.done.value) {
            displayHabit.done.value = displayHabit.done.value.not()
            removeRecord(habitRecord)
        }
    }

    // removes record from the database using the dao (database access object)
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

    private suspend fun resetCompletion(){
        dao.resetCompletion()
    }
}

