package Main;

import Bll.EngineController;
import Bll.EngineFuelRegulator;
import Domain.*;
import Domain.enums.Additive;
import Services.CoreService;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        InitRequest initRequest = new InitRequest("Jaan Pärnust", "jaan@pärnust.ee");
        EngineController engineController = new EngineController(initRequest);
        engineController.StartEngine();
    }

}