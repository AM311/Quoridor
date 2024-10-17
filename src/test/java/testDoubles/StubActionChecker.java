package testDoubles;

import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;

public class StubActionChecker {
  public static ActionChecker<Object> returnAlwaysTrueActionChecker () {
    return (game, target) -> true;
  }

  public static ActionChecker<Object> returnAlwaysFalseActionChecker () {
    return (game, target) -> false;
  }
}
