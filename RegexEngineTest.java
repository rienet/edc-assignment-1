import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RegexEngineTest {
  @Test
  public void evaluatesExpression() {
    RegexEngine regexEngine = new RegexEngine();
    
    Graph stateDiagram = RegexEngine.parseLine("a");
    String eval = RegexEngine.evaluateInput(stateDiagram, "a");
    assertEquals("true", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "ab");
    assertEquals("false", eval);

    stateDiagram = RegexEngine.parseLine("abc 123");
    eval = RegexEngine.evaluateInput(stateDiagram, "abc 123");
    assertEquals("true", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "ab");
    assertEquals("false", eval);

    stateDiagram = RegexEngine.parseLine("'/.;");
    eval = "Invalid input detected";
    assertEquals("Invalid input detected", eval);

    stateDiagram = RegexEngine.parseLine("abc*");
    eval = RegexEngine.evaluateInput(stateDiagram, "abcccc");
    assertEquals("true", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "ab");
    assertEquals("true", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "abc 123");
    assertEquals("false", eval);

    stateDiagram = RegexEngine.parseLine("a+bc");
    eval = RegexEngine.evaluateInput(stateDiagram, "abc");
    assertEquals("true", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "bc");
    assertEquals("false", eval);

    eval = RegexEngine.evaluateInput(stateDiagram, "abcccccccc");
    assertEquals("true", eval);

    stateDiagram = RegexEngine.parseLine("abc+++");
    eval = "Invalid input detected, invalid operator behind operator";
    assertEquals("Invalid input detected, invalid operator behind operator", eval);

    stateDiagram = RegexEngine.parseLine("a+bcd*");
    eval = RegexEngine.evaluateInput(stateDiagram, "aaabc");
    assertEquals("true", eval);
  }
}