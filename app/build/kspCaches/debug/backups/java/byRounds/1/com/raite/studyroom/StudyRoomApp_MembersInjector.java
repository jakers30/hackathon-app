package com.raite.studyroom;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class StudyRoomApp_MembersInjector implements MembersInjector<StudyRoomApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public StudyRoomApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<StudyRoomApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new StudyRoomApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(StudyRoomApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.raite.studyroom.StudyRoomApp.workerFactory")
  public static void injectWorkerFactory(StudyRoomApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
