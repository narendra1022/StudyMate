package com.example.studymate.ui.DI

import com.example.studymate.ui.Repository.Implementation.SessionRepoImpl
import com.example.studymate.ui.Repository.Implementation.SubjectRepoImpl
import com.example.studymate.ui.Repository.Implementation.TaskRepoImpl
import com.example.studymate.ui.Repository.interfaces.SessionRepository
import com.example.studymate.ui.Repository.interfaces.SubjectRepository
import com.example.studymate.ui.Repository.interfaces.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepo(
        impl: SubjectRepoImpl
    ): SubjectRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepo(
        impl: SessionRepoImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepo(
        impl: TaskRepoImpl
    ): TaskRepository

}