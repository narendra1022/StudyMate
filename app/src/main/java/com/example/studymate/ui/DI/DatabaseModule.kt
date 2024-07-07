package com.example.studymate.ui.DI

import android.app.Application
import androidx.room.Room
import com.example.studymate.ui.Dao.SessionDao
import com.example.studymate.ui.Dao.SubjectDao
import com.example.studymate.ui.Dao.TaskDao
import com.example.studymate.ui.Database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            context = application,
            name = "studymate.db",
            klass = AppDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }


}