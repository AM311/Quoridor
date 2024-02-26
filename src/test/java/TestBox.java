import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBox {
  @Test
  void assertGetterTrue() {
    Box box = new Box(false);
    Assertions.assertTrue(box.isTaken());
  }

  @Test
  void assertGetterFalse() {
    Box box = new Box(false);
    Assertions.assertFalse(box.isTaken());
  }

}
