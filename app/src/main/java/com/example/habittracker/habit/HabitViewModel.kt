package com.example.habittracker.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.database.Habit
import com.example.habittracker.database.HabitCompletion
import com.example.habittracker.database.HabitDao
import com.example.habittracker.database.HabitJoin
import com.example.habittracker.database.HabitRecord
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

// modifies the state of the HabitState

//TODO update completion on edti
class HabitViewModel (
    private val dao: HabitDao
): ViewModel() {
    private val _state = MutableStateFlow(HabitState(job = Job()))
    val state = _state
    private val date = fetchdate()

    init {

        viewModelScope.launch {
            flowOf(LocalDate.now()).collect{newDate -> run{
                if (newDate != date){
                    setDate(newDate)
                    if(newDate.dayOfWeek.value == 1){
                        resetCompletion()
                    }else{
                        resetDailyCompletion()
                    }
                }
            }
            }
        }


        viewModelScope.launch {
//             sync data base and state
//             will clean up later
//            dao.insertHabit(Habit(name = "test1", frequency = 1))
//            dao.insertCompletion(HabitCompletion())
//            dao.insertHabit(Habit(name = "test2", frequency = 2,))
//            dao.insertCompletion(HabitCompletion(occurrence = "1011111"))
//            dao.insertHabit(Habit(name = "test3", frequency = 3,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0100000"))
//            dao.insertHabit(Habit(name = "test4", frequency = 4,))
//            dao.insertCompletion(HabitCompletion(occurrence = "0010000"))
        }

//        viewModelScope.launch {
//            // get date from percistent storage
//            viewModelScope.launch {
//                while (true) {
//                    if (state.value.date != LocalDateTime.now()) {
//                        state.update { it.copy(date = LocalDateTime.now()) }
//                        resetCompletion()
//                        if (state.value.date.dayOfWeek.value == 1) {
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
        //val test: String = "_______"
//        val sb = StringBuilder(test)
//        sb.setCharAt(LocalDate.now().dayOfWeek.value-1, '1')
//        Log.d( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
//            sb.toString()
//        )
        _state.value.job = viewModelScope.launch {

            dataSupRoutine()
//            dao.fetchHabitByDay(dayToString(state.value.day)).collect{habitJoin -> run{
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
                val habtiCompletion: HabitCompletion
                if (state.value.editFreq < state.value.edithabitJoin.completion.completion){
                    habtiCompletion = state.value.edithabitJoin.completion.copy(completion = state.value.editFreq)
                    updateCompletion(habtiCompletion)

                }else{
                    habtiCompletion = state.value.edithabitJoin.completion
                }
                val habit = state.value.edithabitJoin.habit.copy(
                    frequency = state.value.editFreq,
                    name = state.value.editString
                )

                checkHabitCompletion(habit, habtiCompletion)
                updateHabit(habit)
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
                        checkHabitCompletion(event.habitJoin.habit, habtiCompletion)
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
            HabitEvent.resetCompletion -> {
                resetDailyCompletion()
            }
            // Deprecated
            HabitEvent.nextDay -> {

                state.value.job.cancel()
                state.update { it.copy(
                    day = state.value.day.inc(),
                    job = viewModelScope.launch { dataSupRoutine() }
                ) }
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

     private suspend fun dataSupRoutine(){
         dao.fetchHabitByDay(dayToString(state.value.day)).collect{habitJoin -> run{
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
        sb.setCharAt(state.value.day, '1')
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
    private fun fetchdate():LocalDate{
        return LocalDate.now()
    }
    private fun setDate(date: LocalDate){

    }
}

