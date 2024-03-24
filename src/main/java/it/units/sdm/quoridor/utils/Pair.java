package it.units.sdm.quoridor.utils;

import java.util.AbstractMap;

public class Pair<K,V> extends AbstractMap.SimpleEntry<K,V> {
  public Pair(K key, V value) {
    super(key, value);
  }
}
