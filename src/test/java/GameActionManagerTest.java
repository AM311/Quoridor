import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.movemanagement.actionmanagers.GameActionManager;
import it.units.sdm.quoridor.utils.ActionController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testDoubles.MockGameActionManager;
import testDoubles.StubAction;
import testDoubles.StubActionChecker;

public class GameActionManagerTest {
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void noChecksRequired_actionIsValid(boolean useOrInsteadOfAnd) throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction()), useOrInsteadOfAnd);
    Assertions.assertTrue(actionManager.isValidAction());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void singleChecksReturnsTrue_actionIsValid(boolean useOrInsteadOfAnd) throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysTrueActionChecker()), useOrInsteadOfAnd);
    Assertions.assertTrue(actionManager.isValidAction());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void singleChecksReturnsFalse_actionIsNotValid(boolean useOrInsteadOfAnd) throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysFalseActionChecker()), useOrInsteadOfAnd);
    Assertions.assertFalse(actionManager.isValidAction());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void multipleChecksReturnsTrue_actionIsValid(boolean useOrInsteadOfAnd) throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysTrueActionChecker(), StubActionChecker.returnAlwaysTrueActionChecker()), useOrInsteadOfAnd);
    Assertions.assertTrue(actionManager.isValidAction());
  }

  @Test
  void multipleChecks_oneReturnsFalse_trueBoolean_actionIsValid() throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysTrueActionChecker(), StubActionChecker.returnAlwaysFalseActionChecker()), true);
    Assertions.assertTrue(actionManager.isValidAction());
  }

  @Test
  void multipleChecks_oneReturnsFalse_falseBoolean_actionIsNotValid() throws InvalidActionException {
    MockGameActionManager actionManager = new MockGameActionManager();
    actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysTrueActionChecker(), StubActionChecker.returnAlwaysFalseActionChecker()), false);
    Assertions.assertFalse(actionManager.isValidAction());
  }

  @Test
  void multipleChecks_oneReturnsFalse_falseBoolean_exceptionIsThrown() {
    ActionManager actionManager = new GameActionManager();
    Assertions.assertThrows(InvalidActionException.class, () -> actionManager.performAction(null, null, new ActionController<>(StubAction.returnEmpyAction(), StubActionChecker.returnAlwaysTrueActionChecker(), StubActionChecker.returnAlwaysFalseActionChecker()), false));
  }
}
