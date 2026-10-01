package com.raite.studyroom.ui.screens.settings;

import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.SettingsRepository;
import com.raite.studyroom.data.sync.Outbox;
import com.raite.studyroom.data.sync.SyncScheduler;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<AuthRepository> authProvider;

  private final Provider<SyncScheduler> syncProvider;

  private final Provider<Outbox> outboxProvider;

  public SettingsViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<AuthRepository> authProvider, Provider<SyncScheduler> syncProvider,
      Provider<Outbox> outboxProvider) {
    this.settingsProvider = settingsProvider;
    this.authProvider = authProvider;
    this.syncProvider = syncProvider;
    this.outboxProvider = outboxProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsProvider.get(), authProvider.get(), syncProvider.get(), outboxProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<SettingsRepository> settingsProvider,
      Provider<AuthRepository> authProvider, Provider<SyncScheduler> syncProvider,
      Provider<Outbox> outboxProvider) {
    return new SettingsViewModel_Factory(settingsProvider, authProvider, syncProvider, outboxProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository settings, AuthRepository auth,
      SyncScheduler sync, Outbox outbox) {
    return new SettingsViewModel(settings, auth, sync, outbox);
  }
}
