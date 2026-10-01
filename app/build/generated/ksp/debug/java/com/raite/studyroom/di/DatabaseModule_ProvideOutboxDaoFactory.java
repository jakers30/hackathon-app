package com.raite.studyroom.di;

import com.raite.studyroom.data.local.StudyRoomDatabase;
import com.raite.studyroom.data.local.dao.OutboxDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvideOutboxDaoFactory implements Factory<OutboxDao> {
  private final Provider<StudyRoomDatabase> dbProvider;

  public DatabaseModule_ProvideOutboxDaoFactory(Provider<StudyRoomDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public OutboxDao get() {
    return provideOutboxDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideOutboxDaoFactory create(
      Provider<StudyRoomDatabase> dbProvider) {
    return new DatabaseModule_ProvideOutboxDaoFactory(dbProvider);
  }

  public static OutboxDao provideOutboxDao(StudyRoomDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideOutboxDao(db));
  }
}
