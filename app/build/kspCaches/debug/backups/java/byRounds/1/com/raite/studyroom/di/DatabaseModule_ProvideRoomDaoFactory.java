package com.raite.studyroom.di;

import com.raite.studyroom.data.local.StudyRoomDatabase;
import com.raite.studyroom.data.local.dao.RoomDao;
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
public final class DatabaseModule_ProvideRoomDaoFactory implements Factory<RoomDao> {
  private final Provider<StudyRoomDatabase> dbProvider;

  public DatabaseModule_ProvideRoomDaoFactory(Provider<StudyRoomDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RoomDao get() {
    return provideRoomDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRoomDaoFactory create(
      Provider<StudyRoomDatabase> dbProvider) {
    return new DatabaseModule_ProvideRoomDaoFactory(dbProvider);
  }

  public static RoomDao provideRoomDao(StudyRoomDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRoomDao(db));
  }
}
