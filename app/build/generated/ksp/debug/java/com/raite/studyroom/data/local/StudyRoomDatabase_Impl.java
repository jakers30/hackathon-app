package com.raite.studyroom.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.raite.studyroom.data.local.dao.OutboxDao;
import com.raite.studyroom.data.local.dao.OutboxDao_Impl;
import com.raite.studyroom.data.local.dao.QuizDao;
import com.raite.studyroom.data.local.dao.QuizDao_Impl;
import com.raite.studyroom.data.local.dao.ResourceDao;
import com.raite.studyroom.data.local.dao.ResourceDao_Impl;
import com.raite.studyroom.data.local.dao.RoomDao;
import com.raite.studyroom.data.local.dao.RoomDao_Impl;
import com.raite.studyroom.data.local.dao.StudyMaterialDao;
import com.raite.studyroom.data.local.dao.StudyMaterialDao_Impl;
import com.raite.studyroom.data.local.dao.TaskDao;
import com.raite.studyroom.data.local.dao.TaskDao_Impl;
import com.raite.studyroom.data.local.dao.UserDao;
import com.raite.studyroom.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudyRoomDatabase_Impl extends StudyRoomDatabase {
  private volatile UserDao _userDao;

  private volatile RoomDao _roomDao;

  private volatile ResourceDao _resourceDao;

  private volatile StudyMaterialDao _studyMaterialDao;

  private volatile QuizDao _quizDao;

  private volatile TaskDao _taskDao;

  private volatile OutboxDao _outboxDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rooms` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `hostId` TEXT NOT NULL, `visibility` TEXT NOT NULL, `inviteCode` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rooms_hostId` ON `rooms` (`hostId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rooms_visibility` ON `rooms` (`visibility`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `room_members` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `userId` TEXT NOT NULL, `role` TEXT NOT NULL, `joinedAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_room_members_roomId` ON `room_members` (`roomId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_room_members_userId` ON `room_members` (`userId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_room_members_roomId_userId` ON `room_members` (`roomId`, `userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `room_allowed_users` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `userId` TEXT NOT NULL, `email` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_room_allowed_users_roomId` ON `room_allowed_users` (`roomId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_room_allowed_users_userId` ON `room_allowed_users` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `resources` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `originalName` TEXT NOT NULL, `displayName` TEXT, `mimeType` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `storagePath` TEXT NOT NULL, `localPath` TEXT, `uploadedBy` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_resources_roomId` ON `resources` (`roomId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `roadmap_items` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `ownerId` TEXT NOT NULL, `order_index` INTEGER NOT NULL, `topic` TEXT NOT NULL, `description` TEXT NOT NULL, `subtopicsJson` TEXT NOT NULL, `completed` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_roadmap_items_roomId` ON `roadmap_items` (`roomId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_roadmap_items_ownerId` ON `roadmap_items` (`ownerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `roadmap_progress` (`id` TEXT NOT NULL, `itemId` TEXT NOT NULL, `userId` TEXT NOT NULL, `completed` INTEGER NOT NULL, `updatedAt` TEXT NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_roadmap_progress_itemId` ON `roadmap_progress` (`itemId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_roadmap_progress_userId` ON `roadmap_progress` (`userId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_roadmap_progress_itemId_userId` ON `roadmap_progress` (`itemId`, `userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reviewers` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `ownerId` TEXT NOT NULL, `sectionsJson` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reviewers_roomId` ON `reviewers` (`roomId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reviewers_ownerId` ON `reviewers` (`ownerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quizzes` (`id` TEXT NOT NULL, `roomId` TEXT NOT NULL, `title` TEXT NOT NULL, `type` TEXT NOT NULL, `status` TEXT NOT NULL, `questionCount` INTEGER NOT NULL, `createdBy` TEXT NOT NULL, `reviewConfirmed` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quizzes_roomId` ON `quizzes` (`roomId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quizzes_status` ON `quizzes` (`status`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_questions` (`id` TEXT NOT NULL, `quizId` TEXT NOT NULL, `order_index` INTEGER NOT NULL, `type` TEXT NOT NULL, `prompt` TEXT NOT NULL, `optionsJson` TEXT NOT NULL, `correctAnswer` TEXT NOT NULL, `explanation` TEXT NOT NULL, `sourceResourceId` TEXT, `sourceResourceName` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quiz_questions_quizId` ON `quiz_questions` (`quizId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` TEXT NOT NULL, `userId` TEXT NOT NULL, `title` TEXT NOT NULL, `notes` TEXT NOT NULL, `priority` TEXT NOT NULL, `dueAt` TEXT, `completed` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_userId` ON `tasks` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `outbox` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entityType` TEXT NOT NULL, `entityId` TEXT NOT NULL, `operation` TEXT NOT NULL, `payloadJson` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `attempts` INTEGER NOT NULL, `roomId` TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_outbox_entityType` ON `outbox` (`entityType`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4125641811cffeb071cda02d7db0bb5a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `rooms`");
        db.execSQL("DROP TABLE IF EXISTS `room_members`");
        db.execSQL("DROP TABLE IF EXISTS `room_allowed_users`");
        db.execSQL("DROP TABLE IF EXISTS `resources`");
        db.execSQL("DROP TABLE IF EXISTS `roadmap_items`");
        db.execSQL("DROP TABLE IF EXISTS `roadmap_progress`");
        db.execSQL("DROP TABLE IF EXISTS `reviewers`");
        db.execSQL("DROP TABLE IF EXISTS `quizzes`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_questions`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `outbox`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(4);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.raite.studyroom.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsRooms = new HashMap<String, TableInfo.Column>(10);
        _columnsRooms.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("hostId", new TableInfo.Column("hostId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("visibility", new TableInfo.Column("visibility", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("inviteCode", new TableInfo.Column("inviteCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRooms.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRooms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRooms = new HashSet<TableInfo.Index>(2);
        _indicesRooms.add(new TableInfo.Index("index_rooms_hostId", false, Arrays.asList("hostId"), Arrays.asList("ASC")));
        _indicesRooms.add(new TableInfo.Index("index_rooms_visibility", false, Arrays.asList("visibility"), Arrays.asList("ASC")));
        final TableInfo _infoRooms = new TableInfo("rooms", _columnsRooms, _foreignKeysRooms, _indicesRooms);
        final TableInfo _existingRooms = TableInfo.read(db, "rooms");
        if (!_infoRooms.equals(_existingRooms)) {
          return new RoomOpenHelper.ValidationResult(false, "rooms(com.raite.studyroom.data.local.entity.RoomEntity).\n"
                  + " Expected:\n" + _infoRooms + "\n"
                  + " Found:\n" + _existingRooms);
        }
        final HashMap<String, TableInfo.Column> _columnsRoomMembers = new HashMap<String, TableInfo.Column>(8);
        _columnsRoomMembers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("joinedAt", new TableInfo.Column("joinedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomMembers.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoomMembers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoomMembers = new HashSet<TableInfo.Index>(3);
        _indicesRoomMembers.add(new TableInfo.Index("index_room_members_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        _indicesRoomMembers.add(new TableInfo.Index("index_room_members_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesRoomMembers.add(new TableInfo.Index("index_room_members_roomId_userId", true, Arrays.asList("roomId", "userId"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoRoomMembers = new TableInfo("room_members", _columnsRoomMembers, _foreignKeysRoomMembers, _indicesRoomMembers);
        final TableInfo _existingRoomMembers = TableInfo.read(db, "room_members");
        if (!_infoRoomMembers.equals(_existingRoomMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "room_members(com.raite.studyroom.data.local.entity.RoomMemberEntity).\n"
                  + " Expected:\n" + _infoRoomMembers + "\n"
                  + " Found:\n" + _existingRoomMembers);
        }
        final HashMap<String, TableInfo.Column> _columnsRoomAllowedUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsRoomAllowedUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoomAllowedUsers.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoomAllowedUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoomAllowedUsers = new HashSet<TableInfo.Index>(2);
        _indicesRoomAllowedUsers.add(new TableInfo.Index("index_room_allowed_users_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        _indicesRoomAllowedUsers.add(new TableInfo.Index("index_room_allowed_users_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoRoomAllowedUsers = new TableInfo("room_allowed_users", _columnsRoomAllowedUsers, _foreignKeysRoomAllowedUsers, _indicesRoomAllowedUsers);
        final TableInfo _existingRoomAllowedUsers = TableInfo.read(db, "room_allowed_users");
        if (!_infoRoomAllowedUsers.equals(_existingRoomAllowedUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "room_allowed_users(com.raite.studyroom.data.local.entity.RoomAllowedUserEntity).\n"
                  + " Expected:\n" + _infoRoomAllowedUsers + "\n"
                  + " Found:\n" + _existingRoomAllowedUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsResources = new HashMap<String, TableInfo.Column>(13);
        _columnsResources.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("originalName", new TableInfo.Column("originalName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("mimeType", new TableInfo.Column("mimeType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("storagePath", new TableInfo.Column("storagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("localPath", new TableInfo.Column("localPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("uploadedBy", new TableInfo.Column("uploadedBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysResources = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesResources = new HashSet<TableInfo.Index>(1);
        _indicesResources.add(new TableInfo.Index("index_resources_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        final TableInfo _infoResources = new TableInfo("resources", _columnsResources, _foreignKeysResources, _indicesResources);
        final TableInfo _existingResources = TableInfo.read(db, "resources");
        if (!_infoResources.equals(_existingResources)) {
          return new RoomOpenHelper.ValidationResult(false, "resources(com.raite.studyroom.data.local.entity.ResourceEntity).\n"
                  + " Expected:\n" + _infoResources + "\n"
                  + " Found:\n" + _existingResources);
        }
        final HashMap<String, TableInfo.Column> _columnsRoadmapItems = new HashMap<String, TableInfo.Column>(12);
        _columnsRoadmapItems.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("ownerId", new TableInfo.Column("ownerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("order_index", new TableInfo.Column("order_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("topic", new TableInfo.Column("topic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("subtopicsJson", new TableInfo.Column("subtopicsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapItems.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoadmapItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoadmapItems = new HashSet<TableInfo.Index>(2);
        _indicesRoadmapItems.add(new TableInfo.Index("index_roadmap_items_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        _indicesRoadmapItems.add(new TableInfo.Index("index_roadmap_items_ownerId", false, Arrays.asList("ownerId"), Arrays.asList("ASC")));
        final TableInfo _infoRoadmapItems = new TableInfo("roadmap_items", _columnsRoadmapItems, _foreignKeysRoadmapItems, _indicesRoadmapItems);
        final TableInfo _existingRoadmapItems = TableInfo.read(db, "roadmap_items");
        if (!_infoRoadmapItems.equals(_existingRoadmapItems)) {
          return new RoomOpenHelper.ValidationResult(false, "roadmap_items(com.raite.studyroom.data.local.entity.RoadmapItemEntity).\n"
                  + " Expected:\n" + _infoRoadmapItems + "\n"
                  + " Found:\n" + _existingRoadmapItems);
        }
        final HashMap<String, TableInfo.Column> _columnsRoadmapProgress = new HashMap<String, TableInfo.Column>(6);
        _columnsRoadmapProgress.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapProgress.put("itemId", new TableInfo.Column("itemId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapProgress.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapProgress.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapProgress.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapProgress.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoadmapProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoadmapProgress = new HashSet<TableInfo.Index>(3);
        _indicesRoadmapProgress.add(new TableInfo.Index("index_roadmap_progress_itemId", false, Arrays.asList("itemId"), Arrays.asList("ASC")));
        _indicesRoadmapProgress.add(new TableInfo.Index("index_roadmap_progress_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesRoadmapProgress.add(new TableInfo.Index("index_roadmap_progress_itemId_userId", true, Arrays.asList("itemId", "userId"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoRoadmapProgress = new TableInfo("roadmap_progress", _columnsRoadmapProgress, _foreignKeysRoadmapProgress, _indicesRoadmapProgress);
        final TableInfo _existingRoadmapProgress = TableInfo.read(db, "roadmap_progress");
        if (!_infoRoadmapProgress.equals(_existingRoadmapProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "roadmap_progress(com.raite.studyroom.data.local.entity.RoadmapProgressEntity).\n"
                  + " Expected:\n" + _infoRoadmapProgress + "\n"
                  + " Found:\n" + _existingRoadmapProgress);
        }
        final HashMap<String, TableInfo.Column> _columnsReviewers = new HashMap<String, TableInfo.Column>(8);
        _columnsReviewers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("ownerId", new TableInfo.Column("ownerId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("sectionsJson", new TableInfo.Column("sectionsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewers.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReviewers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReviewers = new HashSet<TableInfo.Index>(2);
        _indicesReviewers.add(new TableInfo.Index("index_reviewers_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        _indicesReviewers.add(new TableInfo.Index("index_reviewers_ownerId", false, Arrays.asList("ownerId"), Arrays.asList("ASC")));
        final TableInfo _infoReviewers = new TableInfo("reviewers", _columnsReviewers, _foreignKeysReviewers, _indicesReviewers);
        final TableInfo _existingReviewers = TableInfo.read(db, "reviewers");
        if (!_infoReviewers.equals(_existingReviewers)) {
          return new RoomOpenHelper.ValidationResult(false, "reviewers(com.raite.studyroom.data.local.entity.ReviewerEntity).\n"
                  + " Expected:\n" + _infoReviewers + "\n"
                  + " Found:\n" + _existingReviewers);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizzes = new HashMap<String, TableInfo.Column>(12);
        _columnsQuizzes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("roomId", new TableInfo.Column("roomId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("questionCount", new TableInfo.Column("questionCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("createdBy", new TableInfo.Column("createdBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("reviewConfirmed", new TableInfo.Column("reviewConfirmed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizzes.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizzes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizzes = new HashSet<TableInfo.Index>(2);
        _indicesQuizzes.add(new TableInfo.Index("index_quizzes_roomId", false, Arrays.asList("roomId"), Arrays.asList("ASC")));
        _indicesQuizzes.add(new TableInfo.Index("index_quizzes_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        final TableInfo _infoQuizzes = new TableInfo("quizzes", _columnsQuizzes, _foreignKeysQuizzes, _indicesQuizzes);
        final TableInfo _existingQuizzes = TableInfo.read(db, "quizzes");
        if (!_infoQuizzes.equals(_existingQuizzes)) {
          return new RoomOpenHelper.ValidationResult(false, "quizzes(com.raite.studyroom.data.local.entity.QuizEntity).\n"
                  + " Expected:\n" + _infoQuizzes + "\n"
                  + " Found:\n" + _existingQuizzes);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizQuestions = new HashMap<String, TableInfo.Column>(13);
        _columnsQuizQuestions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("quizId", new TableInfo.Column("quizId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("order_index", new TableInfo.Column("order_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("prompt", new TableInfo.Column("prompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("optionsJson", new TableInfo.Column("optionsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("correctAnswer", new TableInfo.Column("correctAnswer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("sourceResourceId", new TableInfo.Column("sourceResourceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("sourceResourceName", new TableInfo.Column("sourceResourceName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizQuestions = new HashSet<TableInfo.Index>(1);
        _indicesQuizQuestions.add(new TableInfo.Index("index_quiz_questions_quizId", false, Arrays.asList("quizId"), Arrays.asList("ASC")));
        final TableInfo _infoQuizQuestions = new TableInfo("quiz_questions", _columnsQuizQuestions, _foreignKeysQuizQuestions, _indicesQuizQuestions);
        final TableInfo _existingQuizQuestions = TableInfo.read(db, "quiz_questions");
        if (!_infoQuizQuestions.equals(_existingQuizQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_questions(com.raite.studyroom.data.local.entity.QuizQuestionEntity).\n"
                  + " Expected:\n" + _infoQuizQuestions + "\n"
                  + " Found:\n" + _existingQuizQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(11);
        _columnsTasks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueAt", new TableInfo.Column("dueAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("deletedAt", new TableInfo.Column("deletedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("syncState", new TableInfo.Column("syncState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(1);
        _indicesTasks.add(new TableInfo.Index("index_tasks_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.raite.studyroom.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsOutbox = new HashMap<String, TableInfo.Column>(8);
        _columnsOutbox.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("entityType", new TableInfo.Column("entityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("entityId", new TableInfo.Column("entityId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("operation", new TableInfo.Column("operation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("payloadJson", new TableInfo.Column("payloadJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("attempts", new TableInfo.Column("attempts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOutbox.put("roomId", new TableInfo.Column("roomId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOutbox = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOutbox = new HashSet<TableInfo.Index>(1);
        _indicesOutbox.add(new TableInfo.Index("index_outbox_entityType", false, Arrays.asList("entityType"), Arrays.asList("ASC")));
        final TableInfo _infoOutbox = new TableInfo("outbox", _columnsOutbox, _foreignKeysOutbox, _indicesOutbox);
        final TableInfo _existingOutbox = TableInfo.read(db, "outbox");
        if (!_infoOutbox.equals(_existingOutbox)) {
          return new RoomOpenHelper.ValidationResult(false, "outbox(com.raite.studyroom.data.local.entity.OutboxEntity).\n"
                  + " Expected:\n" + _infoOutbox + "\n"
                  + " Found:\n" + _existingOutbox);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4125641811cffeb071cda02d7db0bb5a", "29e3486f8ec7aa2c7ca5d465a1999552");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","rooms","room_members","room_allowed_users","resources","roadmap_items","roadmap_progress","reviewers","quizzes","quiz_questions","tasks","outbox");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `rooms`");
      _db.execSQL("DELETE FROM `room_members`");
      _db.execSQL("DELETE FROM `room_allowed_users`");
      _db.execSQL("DELETE FROM `resources`");
      _db.execSQL("DELETE FROM `roadmap_items`");
      _db.execSQL("DELETE FROM `roadmap_progress`");
      _db.execSQL("DELETE FROM `reviewers`");
      _db.execSQL("DELETE FROM `quizzes`");
      _db.execSQL("DELETE FROM `quiz_questions`");
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `outbox`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoomDao.class, RoomDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ResourceDao.class, ResourceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudyMaterialDao.class, StudyMaterialDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizDao.class, QuizDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OutboxDao.class, OutboxDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public RoomDao roomDao() {
    if (_roomDao != null) {
      return _roomDao;
    } else {
      synchronized(this) {
        if(_roomDao == null) {
          _roomDao = new RoomDao_Impl(this);
        }
        return _roomDao;
      }
    }
  }

  @Override
  public ResourceDao resourceDao() {
    if (_resourceDao != null) {
      return _resourceDao;
    } else {
      synchronized(this) {
        if(_resourceDao == null) {
          _resourceDao = new ResourceDao_Impl(this);
        }
        return _resourceDao;
      }
    }
  }

  @Override
  public StudyMaterialDao studyMaterialDao() {
    if (_studyMaterialDao != null) {
      return _studyMaterialDao;
    } else {
      synchronized(this) {
        if(_studyMaterialDao == null) {
          _studyMaterialDao = new StudyMaterialDao_Impl(this);
        }
        return _studyMaterialDao;
      }
    }
  }

  @Override
  public QuizDao quizDao() {
    if (_quizDao != null) {
      return _quizDao;
    } else {
      synchronized(this) {
        if(_quizDao == null) {
          _quizDao = new QuizDao_Impl(this);
        }
        return _quizDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public OutboxDao outboxDao() {
    if (_outboxDao != null) {
      return _outboxDao;
    } else {
      synchronized(this) {
        if(_outboxDao == null) {
          _outboxDao = new OutboxDao_Impl(this);
        }
        return _outboxDao;
      }
    }
  }
}
