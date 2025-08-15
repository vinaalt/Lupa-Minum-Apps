package edu.unikom.lupaminum.repository

import edu.unikom.lupaminum.model.Identity
import edu.unikom.lupaminum.model.Schedule
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor() {
    private var schedule: Schedule? = null

    fun saveSchedule(time: String) {
        schedule = Schedule(time)
    }

    fun getSchedule(): Schedule? = schedule

    fun isScheduleSaved(): Boolean = schedule != null
}