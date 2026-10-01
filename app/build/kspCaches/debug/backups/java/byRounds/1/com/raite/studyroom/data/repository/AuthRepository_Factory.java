package com.raite.studyroom.data.repository;

import com.raite.studyroom.data.local.dao.UserDao;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<SupabaseDataSource> supabaseProvider;

  private final Provider<UserDao> userDaoProvider;

  public AuthRepository_Factory(Provider<SupabaseDataSource> supabaseProvider,
      Provider<UserDao> userDaoProvider) {
    this.supabaseProvider = supabaseProvider;
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(supabaseProvider.get(), userDaoProvider.get());
  }

  public static AuthRepository_Factory create(Provider<SupabaseDataSource> supabaseProvider,
      Provider<UserDao> userDaoProvider) {
    return new AuthRepository_Factory(supabaseProvider, userDaoProvider);
  }

  public static AuthRepository newInstance(SupabaseDataSource supabase, UserDao userDao) {
    return new AuthRepository(supabase, userDao);
  }
}
