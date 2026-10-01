package com.raite.studyroom.di;

import com.raite.studyroom.data.local.StudyRoomDatabase;
import com.raite.studyroom.data.local.dao.ResourceDao;
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
public final class DatabaseModule_ProvideResourceDaoFactory implements Factory<ResourceDao> {
  private final Provider<StudyRoomDatabase> dbProvider;

  public DatabaseModule_ProvideResourceDaoFactory(Provider<StudyRoomDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ResourceDao get() {
    return provideResourceDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideResourceDaoFactory create(
      Provider<StudyRoomDatabase> dbProvider) {
    return new DatabaseModule_ProvideResourceDaoFactory(dbProvider);
  }

  public static ResourceDao provideResourceDao(StudyRoomDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideResourceDao(db));
  }
}
