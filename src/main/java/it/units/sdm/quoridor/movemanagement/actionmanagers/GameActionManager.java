package it.units.sdm.quoridor.movemanagement.actionmanagers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.utils.ActionController;

public class GameActionManager implements ActionManager {

  @Override
  public <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException {
    try {
      //boolean isValidMove = !useOrInsteadOfAnd || actionController.actionCheckers().length == 0;
      boolean isValidMove = false;

      //*LOGICA: ora actionChecer.isValidAction ritorna l'ENUM. La mossa è valida se:
      // - nel caso standard (AND) tutti gli enum = OKAY
      // - nel caso OR almeno un enum = OKAY
      // La mossa NON è valida se:
      // - nel caso AND almeno un Checker è diverso da OKAY -> in questo caso il primo checker diverso da okay determina il messaggio di errore
      // - nel caso ORA TUTTI i checker sono diversi da OKAY -> in questo caso il primo checker diverso da okay determina il messaggio di errore *//

      //caso or: DA FARE
      if (useOrInsteadOfAnd) {
				CheckResult result = null;
        for (ActionChecker<T> actionChecker : actionController.actionCheckers()) {
           result = actionChecker.isValidAction(game, target);
          if (result.equals(CheckResult.OKAY)) {
            isValidMove = true;
            break;
          }
        }
        if (!isValidMove) { //vuol dire che nessun checker è OKAY
          explainInvalidMove(result);
          return;

        }
      } else {
        //*caso end qui sotto*//
        for (ActionChecker<T> actionChecker : actionController.actionCheckers()) {
          CheckResult result = actionChecker.isValidAction(game, target);
          if (!result.equals(CheckResult.OKAY)) {
            explainInvalidMove(result);
          }

        }
      }

        actionController.action().execute(game, target);

    } catch (QuoridorRuntimeException ex) {
      throw new InvalidActionException("Invalid action: " + ex.getMessage());
    }
  }

  private void explainInvalidMove(CheckResult result) throws InvalidActionException {
    throw new InvalidActionException(switch (result) {
      case OKAY -> null;
      case END_OF_AVAIABLE_WALLS -> "You have end the walls that you can place";
      case INVALID_WALL_POSITION -> "The required wall position is invalid";
      case BLOCKING_WALL -> "The required wall position is invalid because it blocks the pawn";
      case INVALID_MOVEMENT -> "You are requiring an invalid movement";
      case OCCUPIED_TILE -> "Your target tile is already occupied so you cannot place there your pawn";
    });
  }
}
