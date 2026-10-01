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
import com.raite.studyroom.data.local.entity.QuizEntity;
import com.raite.studyroom.data.local.entity.QuizQuestionEntity;
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
public final class QuizDao_Impl implements QuizDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfSetStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteQuiz;

  private final SharedSQLiteStatement __preparedStmtOfClearQuestions;

  private final EntityUpsertionAdapter<QuizEntity> __upsertionAdapterOfQuizEntity;

  private final EntityUpsertionAdapter<QuizQuestionEntity> __upsertionAdapterOfQuizQuestionEntity;

  public QuizDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfSetStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE quizzes SET status = ?, reviewConfirmed = ?, syncState = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteQuiz = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM quizzes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearQuestions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM quiz_questions WHERE quizId = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfQuizEntity = new EntityUpsertionAdapter<QuizEntity>(new EntityInsertionAdapter<QuizEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `quizzes` (`id`,`roomId`,`title`,`type`,`status`,`questionCount`,`createdBy`,`reviewConfirmed`,`createdAt`,`updatedAt`,`deletedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getQuestionCount());
        statement.bindString(7, entity.getCreatedBy());
        final int _tmp = entity.getReviewConfirmed() ? 1 : 0;
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
    }, new EntityDeletionOrUpdateAdapter<QuizEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `quizzes` SET `id` = ?,`roomId` = ?,`title` = ?,`type` = ?,`status` = ?,`questionCount` = ?,`createdBy` = ?,`reviewConfirmed` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getRoomId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getQuestionCount());
        statement.bindString(7, entity.getCreatedBy());
        final int _tmp = entity.getReviewConfirmed() ? 1 : 0;
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
    this.__upsertionAdapterOfQuizQuestionEntity = new EntityUpsertionAdapter<QuizQuestionEntity>(new EntityInsertionAdapter<QuizQuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `quiz_questions` (`id`,`quizId`,`order_index`,`type`,`prompt`,`optionsJson`,`correctAnswer`,`explanation`,`sourceResourceId`,`sourceResourceName`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizQuestionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getQuizId());
        statement.bindLong(3, entity.getOrder());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getPrompt());
        statement.bindString(6, entity.getOptionsJson());
        statement.bindString(7, entity.getCorrectAnswer());
        statement.bindString(8, entity.getExplanation());
        if (entity.getSourceResourceId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSourceResourceId());
        }
        if (entity.getSourceResourceName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSourceResourceName());
        }
        statement.bindString(11, entity.getCreatedAt());
        statement.bindString(12, entity.getUpdatedAt());
        statement.bindString(13, entity.getSyncState());
      }
    }, new EntityDeletionOrUpdateAdapter<QuizQuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `quiz_questions` SET `id` = ?,`quizId` = ?,`order_index` = ?,`type` = ?,`prompt` = ?,`optionsJson` = ?,`correctAnswer` = ?,`explanation` = ?,`sourceResourceId` = ?,`sourceResourceName` = ?,`createdAt` = ?,`updatedAt` = ?,`syncState` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizQuestionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getQuizId());
        statement.bindLong(3, entity.getOrder());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getPrompt());
        statement.bindString(6, entity.getOptionsJson());
        statement.bindString(7, entity.getCorrectAnswer());
        statement.bindString(8, entity.getExplanation());
        if (entity.getSourceResourceId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSourceResourceId());
        }
        if (entity.getSourceResourceName() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSourceResourceName());
        }
        statement.bindString(11, entity.getCreatedAt());
        statement.bindString(12, entity.getUpdatedAt());
        statement.bindString(13, entity.getSyncState());
        statement.bindString(14, entity.getId());
      }
    });
  }

  @Override
  public Object setStatus(final String id, final String status, final boolean confirmed,
      final String syncState, final String updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        final int _tmp = confirmed ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, syncState);
        _argIndex = 4;
        _stmt.bindString(_argIndex, updatedAt);
        _argIndex = 5;
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
          __preparedStmtOfSetStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteQuiz(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteQuiz.acquire();
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
          __preparedStmtOfDeleteQuiz.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearQuestions(final String quizId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearQuestions.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, quizId);
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
          __preparedStmtOfClearQuestions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuizzes(final List<QuizEntity> quizzes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuizEntity.upsert(quizzes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuiz(final QuizEntity quiz, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuizEntity.upsert(quiz);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuestions(final List<QuizQuestionEntity> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuizQuestionEntity.upsert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuestion(final QuizQuestionEntity question,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuizQuestionEntity.upsert(question);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<QuizEntity>> observeQuizzes(final String roomId) {
    final String _sql = "SELECT * FROM quizzes WHERE roomId = ? AND deletedAt IS NULL ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, roomId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quizzes"}, new Callable<List<QuizEntity>>() {
      @Override
      @NonNull
      public List<QuizEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "questionCount");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfReviewConfirmed = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewConfirmed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final List<QuizEntity> _result = new ArrayList<QuizEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuestionCount;
            _tmpQuestionCount = _cursor.getInt(_cursorIndexOfQuestionCount);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final boolean _tmpReviewConfirmed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReviewConfirmed);
            _tmpReviewConfirmed = _tmp != 0;
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
            _item = new QuizEntity(_tmpId,_tmpRoomId,_tmpTitle,_tmpType,_tmpStatus,_tmpQuestionCount,_tmpCreatedBy,_tmpReviewConfirmed,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
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
  public Object getQuiz(final String id, final Continuation<? super QuizEntity> $completion) {
    final String _sql = "SELECT * FROM quizzes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<QuizEntity>() {
      @Override
      @Nullable
      public QuizEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "questionCount");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfReviewConfirmed = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewConfirmed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final QuizEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuestionCount;
            _tmpQuestionCount = _cursor.getInt(_cursorIndexOfQuestionCount);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final boolean _tmpReviewConfirmed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReviewConfirmed);
            _tmpReviewConfirmed = _tmp != 0;
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
            _result = new QuizEntity(_tmpId,_tmpRoomId,_tmpTitle,_tmpType,_tmpStatus,_tmpQuestionCount,_tmpCreatedBy,_tmpReviewConfirmed,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
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

  @Override
  public Flow<QuizEntity> observeQuiz(final String id) {
    final String _sql = "SELECT * FROM quizzes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quizzes"}, new Callable<QuizEntity>() {
      @Override
      @Nullable
      public QuizEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "questionCount");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfReviewConfirmed = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewConfirmed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final QuizEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpRoomId;
            _tmpRoomId = _cursor.getString(_cursorIndexOfRoomId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuestionCount;
            _tmpQuestionCount = _cursor.getInt(_cursorIndexOfQuestionCount);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final boolean _tmpReviewConfirmed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReviewConfirmed);
            _tmpReviewConfirmed = _tmp != 0;
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
            _result = new QuizEntity(_tmpId,_tmpRoomId,_tmpTitle,_tmpType,_tmpStatus,_tmpQuestionCount,_tmpCreatedBy,_tmpReviewConfirmed,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSyncState);
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
  public Flow<List<QuizQuestionEntity>> observeQuestions(final String quizId) {
    final String _sql = "SELECT * FROM quiz_questions WHERE quizId = ? ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, quizId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_questions"}, new Callable<List<QuizQuestionEntity>>() {
      @Override
      @NonNull
      public List<QuizQuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuizId = CursorUtil.getColumnIndexOrThrow(_cursor, "quizId");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfOptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "optionsJson");
          final int _cursorIndexOfCorrectAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfSourceResourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceResourceId");
          final int _cursorIndexOfSourceResourceName = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceResourceName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSyncState = CursorUtil.getColumnIndexOrThrow(_cursor, "syncState");
          final List<QuizQuestionEntity> _result = new ArrayList<QuizQuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizQuestionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpQuizId;
            _tmpQuizId = _cursor.getString(_cursorIndexOfQuizId);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpOptionsJson;
            _tmpOptionsJson = _cursor.getString(_cursorIndexOfOptionsJson);
            final String _tmpCorrectAnswer;
            _tmpCorrectAnswer = _cursor.getString(_cursorIndexOfCorrectAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpSourceResourceId;
            if (_cursor.isNull(_cursorIndexOfSourceResourceId)) {
              _tmpSourceResourceId = null;
            } else {
              _tmpSourceResourceId = _cursor.getString(_cursorIndexOfSourceResourceId);
            }
            final String _tmpSourceResourceName;
            if (_cursor.isNull(_cursorIndexOfSourceResourceName)) {
              _tmpSourceResourceName = null;
            } else {
              _tmpSourceResourceName = _cursor.getString(_cursorIndexOfSourceResourceName);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpSyncState;
            _tmpSyncState = _cursor.getString(_cursorIndexOfSyncState);
            _item = new QuizQuestionEntity(_tmpId,_tmpQuizId,_tmpOrder,_tmpType,_tmpPrompt,_tmpOptionsJson,_tmpCorrectAnswer,_tmpExplanation,_tmpSourceResourceId,_tmpSourceResourceName,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
