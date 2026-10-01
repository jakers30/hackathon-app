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
import com.raite.studyroom.data.local.entity.ResourceEntity;
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
public final class ResourceDao_Impl implements ResourceDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfSetLocalPath;

  private final SharedSQLiteStatement __preparedStmtOfSetDisplayName;

  private final SharedSQLiteStatement __preparedStmtOfDeleteResource;

  private final EntityUpsertionAdapter<ResourceEntity> __upsertionAdapterOfResourceEntity;

  public ResourceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfSetLocalPath = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE resources SET localPath = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetDisplayName = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE resources SET displayName = ?, syncState = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteResource = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM resources WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfResourceEntity = new EntityUpsertionAdapter<ResourceEntity>(new EntityInsertionAdapter<ResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `resources` (`id`,`roomId`,`originalName`,`displayName`,`mimeType`,`sizeBytes`,`storagePath`,`localPath`,`uploadedBy`,`createdAt`,`updatedAt`,`deletedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOriginalName());
        if (entity.getDisplayName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDisplayName());
        }
        statement.bindString(5, entity.getMimeType());
        statement.bindLong(6, entity.getSizeBytes());
        statement.bindString(7, entity.getStoragePath());
        if (entity.getLocalPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocalPath());
        }
        statement.bindString(9, entity.getUploadedBy());
        statement.bindString(10, entity.getCreatedAt());
        statement.bindString(11, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getDeletedAt());
        }
        statement.bindString(13, entity.getSyncState());
      }
    }, new EntityDeletionOrUpdateAdapter<ResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `resources` SET `id` = ?,`roomId` = ?,`originalName` = ?,`displayName` = ?,`mimeType` = ?,`sizeBytes` = ?,`storagePath` = ?,`localPath` = ?,`uploadedBy` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getOriginalName());
        if (entity.getDisplayName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDisplayName());
        }
        statement.bindString(5, entity.getMimeType());
        statement.bindLong(6, entity.getSizeBytes());
        statement.bindString(7, entity.getStoragePath());
        if (entity.getLocalPath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLocalPath());
        }
        statement.bindString(9, entity.getUploadedBy());
        statement.bindString(10, entity.getCreatedAt());
        statement.bindString(11, entity.getUpdatedAt());
        if (entity.getDeletedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getDeletedAt());
        }
        statement.bindString(13, entity.getSyncState());
        statement.bindString(14, entity.getId());
      }
    });
  }

  @Override
  public Object setLocalPath(final String id, final String path,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetLocalPath.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, path);
        _argIndex = 2;
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
          __preparedStmtOfSetLocalPath.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setDisplayName(final String id, final String displayName, final String syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetDisplayName.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, displayName);
        _argIndex = 2;
        _stmt.bindString(_argIndex, syncState);
        _argIndex = 3;
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
          __preparedStmtOfSetDisplayName.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteResource(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteResource.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteResource.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertResources(final List<ResourceEntity> resources,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfResourceEntity.upsert(resources);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertResource(final ResourceEntity resource,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfResourceEntity.upsert(resource);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ResourceEntity>> observeResources(final String roomId) {
    final String _sql = "SELECT * FROM resources WHERE roomId = ? AND deletedAt IS NULL ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resources"}, new Callable<List<ResourceEntity>>() {
      @Override
      @NonNull
      public List<ResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOriginalName = CursorUtil.getColumnIndexOrThrow(_cursor, "originalName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfStoragePath = CursorUtil.getColumnIndexOrThrow(_cursor, "storagePath");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfUploadedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadedBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final List<ResourceEntity> _result = new ArrayList<ResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ResourceEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOriginalName;
            _tmpOriginalName = _cursor.getString(_cursorIndexOfOriginalName);
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpStoragePath;
            _tmpStoragePath = _cursor.getString(_cursorIndexOfStoragePath);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpUploadedBy;
            _tmpUploadedBy = _cursor.getString(_cursorIndexOfUploadedBy);
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
            _item = new ResourceEntity(_tmpId,_tmpRoomId,_tmpOriginalName,_tmpDisplayName,_tmpMimeType,_tmpSizeBytes,_tmpStoragePath,_tmpLocalPath,_tmpUploadedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
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
  public Object getResource(final String id,
      final Continuation<? super ResourceEntity> $completion) {
    final String _sql = "SELECT * FROM resources WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ResourceEntity>() {
      @Override
      @Nullable
      public ResourceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfOriginalName = CursorUtil.getColumnIndexOrThrow(_cursor, "originalName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfStoragePath = CursorUtil.getColumnIndexOrThrow(_cursor, "storagePath");
          final int _cursorIndexOfLocalPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPath");
          final int _cursorIndexOfUploadedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadedBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final ResourceEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpOriginalName;
            _tmpOriginalName = _cursor.getString(_cursorIndexOfOriginalName);
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final String _tmpMimeType;
            _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            final long _tmpSizeBytes;
            _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            final String _tmpStoragePath;
            _tmpStoragePath = _cursor.getString(_cursorIndexOfStoragePath);
            final String _tmpLocalPath;
            if (_cursor.isNull(_cursorIndexOfLocalPath)) {
              _tmpLocalPath = null;
            } else {
              _tmpLocalPath = _cursor.getString(_cursorIndexOfLocalPath);
            }
            final String _tmpUploadedBy;
            _tmpUploadedBy = _cursor.getString(_cursorIndexOfUploadedBy);
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
            _result = new ResourceEntity(_tmpId,_tmpRoomId,_tmpOriginalName,_tmpDisplayName,_tmpMimeType,_tmpSizeBytes,_tmpStoragePath,_tmpLocalPath,_tmpUploadedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
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
