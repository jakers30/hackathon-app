package com.raite.studyroom.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.raite.studyroom.data.local.entity.ReviewerEntity;
import com.raite.studyroom.data.local.entity.RoadmapItemEntity;
import com.raite.studyroom.data.local.entity.RoadmapProgressEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudyMaterialDao_Impl implements StudyMaterialDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfSetCompleted;

  private final SharedSQLiteStatement __preparedStmtOfClearRoadmap;

  private final SharedSQLiteStatement __preparedStmtOfClearReviewer;

  private final EntityUpsertionAdapter<RoadmapItemEntity> __upsertionAdapterOfRoadmapItemEntity;

  private final EntityUpsertionAdapter<RoadmapProgressEntity> __upsertionAdapterOfRoadmapProgressEntity;

  private final EntityUpsertionAdapter<ReviewerEntity> __upsertionAdapterOfReviewerEntity;

  public StudyMaterialDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfSetCompleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE roadmap_items SET completed = ?, syncState = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearRoadmap = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM roadmap_items WHERE roomId = ? AND ownerId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearReviewer = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reviewers WHERE roomId = ? AND ownerId = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfRoadmapItemEntity = new EntityUpsertionAdapter<RoadmapItemEntity>(new EntityInsertionAdapter<RoadmapItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `roadmap_items` (`id`,`roomId`,`ownerId`,`order_index`,`topic`,`description`,`subtopicsJson`,`completed`,`createdAt`,`updatedAt`,`deletedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoadmapItemEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOwnerId());
        statement.bindLong(4, entity.getOrder());
        statement.bindString(5, entity.getTopic());
        statement.bindString(6, entity.getDescription());
        statement.bindString(7, entity.getSubtopicsJson());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getCreatedAt());
        statement.bindString(10, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDeletedAt());
        }
        statement.bindString(12, entity.getSyncState());
      }
    }, new EntityDeletionOrUpdateAdapter<RoadmapItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `roadmap_items` SET `id` = ?,`roomId` = ?,`ownerId` = ?,`order_index` = ?,`topic` = ?,`description` = ?,`subtopicsJson` = ?,`completed` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoadmapItemEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOwnerId());
        statement.bindLong(4, entity.getOrder());
        statement.bindString(5, entity.getTopic());
        statement.bindString(6, entity.getDescription());
        statement.bindString(7, entity.getSubtopicsJson());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getCreatedAt());
        statement.bindString(10, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDeletedAt());
        }
        statement.bindString(12, entity.getSyncState());
        statement.bindString(13, entity.getId());
      }
    });
    this.__upsertionAdapterOfRoadmapProgressEntity = new EntityUpsertionAdapter<RoadmapProgressEntity>(new EntityInsertionAdapter<RoadmapProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `roadmap_progress` (`id`,`itemId`,`userId`,`completed`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoadmapProgressEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getItemId());
        statement.bindString(3, entity.getUserId());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getUpdatedAt());
        statement.bindString(6, entity.getSyncState());
      }
    }, new EntityDeletionOrUpdateAdapter<RoadmapProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `roadmap_progress` SET `id` = ?,`itemId` = ?,`userId` = ?,`completed` = ?,`updatedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoadmapProgressEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getItemId());
        statement.bindString(3, entity.getUserId());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getUpdatedAt());
        statement.bindString(6, entity.getSyncState());
        statement.bindString(7, entity.getId());
      }
    });
    this.__upsertionAdapterOfReviewerEntity = new EntityUpsertionAdapter<ReviewerEntity>(new EntityInsertionAdapter<ReviewerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `reviewers` (`id`,`roomId`,`ownerId`,`sectionsJson`,`createdAt`,`updatedAt`,`deletedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReviewerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOwnerId());
        statement.bindString(4, entity.getSectionsJson());
        statement.bindString(5, entity.getCreatedAt());
        statement.bindString(6, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDeletedAt());
        }
        statement.bindString(8, entity.getSyncState());
      }
    }, new EntityDeletionOrUpdateAdapter<ReviewerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `reviewers` SET `id` = ?,`roomId` = ?,`ownerId` = ?,`sectionsJson` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReviewerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOwnerId());
        statement.bindString(4, entity.getSectionsJson());
        statement.bindString(5, entity.getCreatedAt());
        statement.bindString(6, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDeletedAt());
        }
        statement.bindString(8, entity.getSyncState());
        statement.bindString(9, entity.getId());
      }
    });
  }

  @Override
  public Object setCompleted(final String id, final boolean completed, final String syncState,
      final String updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetCompleted.acquire();
        int _argIndex = 1;
        final int _tmp = completed ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, syncState);
        _argIndex = 3;
        _stmt.bindString(_argIndex, updatedAt);
        _argIndex = 4;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetCompleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearRoadmap(final String roomId, final String ownerId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearRoadmap.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, roomId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, ownerId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearRoadmap.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearReviewer(final String roomId, final String ownerId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearReviewer.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, roomId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, ownerId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearReviewer.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertRoadmapItems(final List<RoadmapItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfRoadmapItemEntity.upsert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertProgress(final List<RoadmapProgressEntity> progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfRoadmapProgressEntity.upsert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertReviewer(final ReviewerEntity reviewer,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfReviewerEntity.upsert(reviewer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RoadmapItemEntity>> observeRoadmap(final String roomId, final String ownerId) {
    final String _sql = "SELECT * FROM roadmap_items WHERE roomId = ? AND ownerId = ? AND deletedAt IS NULL ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    _argIndex = 2;
    _statement.bindString(_argIndex, ownerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"roadmap_items"}, new Callable<List<RoadmapItemEntity>>() {
      @Override
      @NonNull
      public List<RoadmapItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSubtopicsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "subtopicsJson");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final List<RoadmapItemEntity> _result = new ArrayList<RoadmapItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RoadmapItemEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOwnerId;
            _tmpOwnerId = _cursor.getString(_cursorIndexOfOwnerId);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final String _tmpTopic;
            _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSubtopicsJson;
            _tmpSubtopicsJson = _cursor.getString(_cursorIndexOfSubtopicsJson);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getString(_cursorIndexOfDeletedAt);
            }
            final String _tmpSyncState;
            _tmpSyncState = _cursor.getString(_cursorIndexOfSyncState);
            _item = new RoadmapItemEntity(_tmpId,_tmpRoomId,_tmpOwnerId,_tmpOrder,_tmpTopic,_tmpDescription,_tmpSubtopicsJson,_tmpCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRoadmap(final String roomId, final String ownerId,
      final Continuation<? super List<RoadmapItemEntity>> $completion) {
    final String _sql = "SELECT * FROM roadmap_items WHERE roomId = ? AND ownerId = ? AND deletedAt IS NULL ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    _argIndex = 2;
    _statement.bindString(_argIndex, ownerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RoadmapItemEntity>>() {
      @Override
      @NonNull
      public List<RoadmapItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSubtopicsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "subtopicsJson");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final List<RoadmapItemEntity> _result = new ArrayList<RoadmapItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RoadmapItemEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOwnerId;
            _tmpOwnerId = _cursor.getString(_cursorIndexOfOwnerId);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final String _tmpTopic;
            _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSubtopicsJson;
            _tmpSubtopicsJson = _cursor.getString(_cursorIndexOfSubtopicsJson);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getString(_cursorIndexOfDeletedAt);
            }
            final String _tmpSyncState;
            _tmpSyncState = _cursor.getString(_cursorIndexOfSyncState);
            _item = new RoadmapItemEntity(_tmpId,_tmpRoomId,_tmpOwnerId,_tmpOrder,_tmpTopic,_tmpDescription,_tmpSubtopicsJson,_tmpCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ReviewerEntity> observeReviewer(final String roomId, final String ownerId) {
    final String _sql = "SELECT * FROM reviewers WHERE roomId = ? AND ownerId = ? AND deletedAt IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    _argIndex = 2;
    _statement.bindString(_argIndex, ownerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reviewers"}, new Callable<ReviewerEntity>() {
      @Override
      @Nullable
      public ReviewerEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfSectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sectionsJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final ReviewerEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOwnerId;
            _tmpOwnerId = _cursor.getString(_cursorIndexOfOwnerId);
            final String _tmpSectionsJson;
            _tmpSectionsJson = _cursor.getString(_cursorIndexOfSectionsJson);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getString(_cursorIndexOfDeletedAt);
            }
            final String _tmpSyncState;
            _tmpSyncState = _cursor.getString(_cursorIndexOfSyncState);
            _result = new ReviewerEntity(_tmpId,_tmpRoomId,_tmpOwnerId,_tmpSectionsJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getReviewer(final String roomId, final String ownerId,
      final Continuation<? super ReviewerEntity> $completion) {
    final String _sql = "SELECT * FROM reviewers WHERE roomId = ? AND ownerId = ? AND deletedAt IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    _argIndex = 2;
    _statement.bindString(_argIndex, ownerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReviewerEntity>() {
      @Override
      @Nullable
      public ReviewerEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfSectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sectionsJson");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final ReviewerEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOwnerId;
            _tmpOwnerId = _cursor.getString(_cursorIndexOfOwnerId);
            final String _tmpSectionsJson;
            _tmpSectionsJson = _cursor.getString(_cursorIndexOfSectionsJson);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpDeletedAt;
            if (_cursor.isNull(_cursorIndexOfDeletedAt)) {
              _tmpDeletedAt = null;
            } else {
              _tmpDeletedAt = _cursor.getString(_cursorIndexOfDeletedAt);
            }
            final String _tmpSyncState;
            _tmpSyncState = _cursor.getString(_cursorIndexOfSyncState);
            _result = new ReviewerEntity(_tmpId,_tmpRoomId,_tmpOwnerId,_tmpSectionsJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
