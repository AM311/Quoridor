import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.view.cli.InformationStringBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InformationStringBuilderTest {
  @Test
  void stringIsNotNull() {
    String result = InformationStringBuilder.start().build();
    Assertions.assertNotNull(result);
  }
  @Test
  void stringWithoutAppendIsBlank() {
    String result = InformationStringBuilder.start().build();
    Assertions.assertTrue(result.isBlank());
  }

  @Test
  void stringWithTitleAppendIsNotBlank() {
    String result = InformationStringBuilder.start().appendTitle().build();
    Assertions.assertFalse(result.isBlank());
  }

  @Test
  void stringWithGameRulesAppendIsNotBlank() {
    String result = InformationStringBuilder.start().appendGameRules().build();
    Assertions.assertFalse(result.isBlank());
  }

  @Test
  void stringWithWallsConventionAppendIsNotBlank() {
    String result = InformationStringBuilder.start().appendWallsConvention().build();
    Assertions.assertFalse(result.isBlank());
  }

  @Test
  void stringAfterNewStartIsBlank() {
    InformationStringBuilder.start().appendTitle().build();
    String result2 = InformationStringBuilder.start().build();
    Assertions.assertTrue(result2.isBlank());
  }
}
