package com.example.studymate.ui.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studymate.ui.Dao.SessionDao
import com.example.studymate.ui.Dao.SubjectDao
import com.example.studymate.ui.Dao.TaskDao
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject
import com.example.studymate.ui.Models.Task
import com.example.studymate.ui.Util.colorListToStringConverter

@Database(
    entities = [Subject::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(colorListToStringConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

    abstract fun sessionDao(): SessionDao

    abstract fun taskDao(): TaskDao

}