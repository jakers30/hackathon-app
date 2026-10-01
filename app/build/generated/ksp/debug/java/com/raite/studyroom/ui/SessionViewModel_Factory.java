package com.raite.studyroom.ui;

import com.raite.studyroom.data.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SessionViewModel_Factory implements Factory<SessionViewModel> {
  private final Provider<AuthRepository> authProvider;

  public SessionViewModel_Factory(Provider<AuthRepository> authProvider) {
    this.authProvider = authProvider;
  }

  @Override
  public SessionViewModel get() {
    return newInstance(authProvider.get());
  }

  public static SessionViewModel_Factory create(Provider<AuthRepository> authProvider) {
    return new SessionViewModel_Factory(authProvider);
  }

  public static SessionViewModel newInstance(AuthRepository auth) {
    return new SessionViewModel(auth);
  }
}
