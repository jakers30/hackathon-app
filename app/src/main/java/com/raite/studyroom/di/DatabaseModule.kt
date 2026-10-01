package com.raite.studyroom.di

import android.content.Context
import androidx.room.Room
import com.raite.studyroom.data.local.StudyRoomDatabase
import com.raite.studyroom.data.local.dao.OutboxDao
import com.raite.studyroom.data.local.dao.QuizDao
import com.raite.studyroom.data.local.dao.ResourceDao
import com.raite.studyroom.data.local.dao.RoomDao
import com.raite.studyroom.data.local.dao.StudyMaterialDao
import com.raite.studyroom.data.local.dao.TaskDao
import com.raite.studyroom.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StudyRoomDatabase =
        Room.databaseBuilder(context, StudyRoomDatabase::class.java, "studyroom.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserDao(db: StudyRoomDatabase): UserDao = db.userDao()
    @Provides fun provideRoomDao(db: StudyRoomDatabase): RoomDao = db.roomDao()
    @Provides fun provideResourceDao(db: StudyRoomDatabase): ResourceDao = db.resourceDao()
    @Provides fun provideStudyMaterialDao(db: StudyRoomDatabase): StudyMaterialDao = db.studyMaterialDao()
    @Provides fun provideQuizDao(db: StudyRoomDatabase): QuizDao = db.quizDao()
    @Provides fun provideTaskDao(db: StudyRoomDatabase): TaskDao = db.taskDao()
    @Provides fun provideOutboxDao(db: StudyRoomDatabase): OutboxDao = db.outboxDao()
}
