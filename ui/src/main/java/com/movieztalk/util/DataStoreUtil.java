package com.movieztalk.util;

import java.util.UUID;

public class DataStoreUtil {

  public static synchronized String buildUniqueId() {
    return UUID.randomUUID().toString();
  }
}
