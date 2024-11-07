package testDoubles;

import it.units.sdm.quoridor.movemanagement.actions.Action;

public class StubAction {
  public static Action<Object> returnEmpyAction () {
    return (game, target) -> {
      //Empty body
    };
  }
}
