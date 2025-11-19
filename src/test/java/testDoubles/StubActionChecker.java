package testDoubles;

import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.CheckResult;

public class StubActionChecker {
  public enum StubCheckResult implements CheckResult{
    OKAY, KO
  }
  public static ActionChecker<Object> returnAlwaysOKAYActionChecker () {
    return (game, target) -> StubCheckResult.OKAY;
  }

  public static ActionChecker<Object> returnAlwaysKOActionChecker () {
    return (game, target) -> StubCheckResult.KO;
  }
}
