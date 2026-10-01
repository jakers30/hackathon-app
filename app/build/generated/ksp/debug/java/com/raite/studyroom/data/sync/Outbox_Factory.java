package com.raite.studyroom.data.sync;

import com.raite.studyroom.data.local.dao.OutboxDao;
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
public final class Outbox_Factory implements Factory<Outbox> {
  private final Provider<OutboxDao> daoProvider;

  public Outbox_Factory(Provider<OutboxDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public Outbox get() {
    return newInstance(daoProvider.get());
  }

  public static Outbox_Factory create(Provider<OutboxDao> daoProvider) {
    return new Outbox_Factory(daoProvider);
  }

  public static Outbox newInstance(OutboxDao dao) {
    return new Outbox(dao);
  }
}
