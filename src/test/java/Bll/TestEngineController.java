package Bll;

import Domain.InitRequest;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestEngineController {

    @Test(invocationCount = 10)
    public void testRegulator() {
        InitRequest initRequest = new InitRequest("Jaan Pärnust", "jaan@pärnust.ee");
        EngineController engineController = new EngineController(initRequest);
        assertDoesNotThrow(engineController::StartEngine);
    }
}
