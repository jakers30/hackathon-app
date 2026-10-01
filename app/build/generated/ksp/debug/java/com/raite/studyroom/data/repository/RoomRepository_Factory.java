package com.raite.studyroom.data.repository;

import com.raite.studyroom.data.local.dao.RoomDao;
import com.raite.studyroom.data.remote.SupabaseDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RoomRepository_Factory implements Factory<RoomRepository> {
  private final Provider<SupabaseDataSource> supabaseProvider;

  private final Provider<RoomDao> roomDaoProvider;

  public RoomRepository_Factory(Provider<SupabaseDataSource> supabaseProvider,
      Provider<RoomDao> roomDaoProvider) {
    this.supabaseProvider = supabaseProvider;
    this.roomDaoProvider = roomDaoProvider;
  }

  @Override
  public RoomRepository get() {
    return newInstance(supabaseProvider.get(), roomDaoProvider.get());
  }

  public static RoomRepository_Factory create(Provider<SupabaseDataSource> supabaseProvider,
      Provider<RoomDao> roomDaoProvider) {
    return new RoomRepository_Factory(supabaseProvider, roomDaoProvider);
  }

  public static RoomRepository newInstance(SupabaseDataSource supabase, RoomDao roomDao) {
    return new RoomRepository(supabase, roomDao);
  }
}
