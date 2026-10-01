package com.raite.studyroom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raite.studyroom.data.local.dao.OutboxDao
import com.raite.studyroom.data.local.dao.QuizDao
import com.raite.studyroom.data.local.dao.ResourceDao
import com.raite.studyroom.data.local.dao.RoomDao
import com.raite.studyroom.data.local.dao.StudyMaterialDao
import com.raite.studyroom.data.local.dao.TaskDao
import com.raite.studyroom.data.local.dao.UserDao
import com.raite.studyroom.data.local.entity.OutboxEntity
import com.raite.studyroom.data.local.entity.QuizEntity
import com.raite.studyroom.data.local.entity.QuizQuestionEntity
import com.raite.studyroom.data.local.entity.ResourceEntity
import com.raite.studyroom.data.local.entity.ReviewerEntity
import com.raite.studyroom.data.local.entity.RoadmapItemEntity
import com.raite.studyroom.data.local.entity.RoadmapProgressEntity
import com.raite.studyroom.data.local.entity.RoomAllowedUserEntity
import com.raite.studyroom.data.local.entity.RoomEntity
import com.raite.studyroom.data.local.entity.RoomMemberEntity
import com.raite.studyroom.data.local.entity.TaskEntity
import com.raite.studyroom.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RoomEntity::class,
        RoomMemberEntity::class,
        RoomAllowedUserEntity::class,
        ResourceEntity::class,
        RoadmapItemEntity::class,
        RoadmapProgressEntity::class,
        ReviewerEntity::class,
        QuizEntity::class,
        QuizQuestionEntity::class,
        TaskEntity::class,
        OutboxEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class StudyRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
    abstract fun resourceDao(): ResourceDao
    abstract fun studyMaterialDao(): StudyMaterialDao
    abstract fun quizDao(): QuizDao
    abstract fun taskDao(): TaskDao
    abstract fun outboxDao(): OutboxDao
}
