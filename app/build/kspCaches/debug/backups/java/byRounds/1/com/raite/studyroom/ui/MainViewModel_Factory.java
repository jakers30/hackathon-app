package com.raite.studyroom.ui;

import com.raite.studyroom.data.repository.SettingsRepository;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  public MainViewModel_Factory(Provider<SettingsRepository> settingsProvider) {
    this.settingsProvider = settingsProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(settingsProvider.get());
  }

  public static MainViewModel_Factory create(Provider<SettingsRepository> settingsProvider) {
    return new MainViewModel_Factory(settingsProvider);
  }

  public static MainViewModel newInstance(SettingsRepository settings) {
    return new MainViewModel(settings);
  }
}
