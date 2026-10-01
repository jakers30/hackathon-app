package com.raite.studyroom.ui.screens.rooms;

import com.raite.studyroom.data.repository.AuthRepository;
import com.raite.studyroom.data.repository.RoomRepository;
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
public final class CreateRoomViewModel_Factory implements Factory<CreateRoomViewModel> {
  private final Provider<AuthRepository> authProvider;

  private final Provider<RoomRepository> roomsProvider;

  public CreateRoomViewModel_Factory(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomsProvider) {
    this.authProvider = authProvider;
    this.roomsProvider = roomsProvider;
  }

  @Override
  public CreateRoomViewModel get() {
    return newInstance(authProvider.get(), roomsProvider.get());
  }

  public static CreateRoomViewModel_Factory create(Provider<AuthRepository> authProvider,
      Provider<RoomRepository> roomsProvider) {
    return new CreateRoomViewModel_Factory(authProvider, roomsProvider);
  }

  public static CreateRoomViewModel newInstance(AuthRepository auth, RoomRepository rooms) {
    return new CreateRoomViewModel(auth, rooms);
  }
}
