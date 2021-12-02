package Bll;

import Domain.EngineData;
import Domain.EngineRequest;
import Domain.InitRequest;
import Domain.InitResponse;
import Domain.enums.Additive;
import Services.CoreService;

public class EngineController {

    private final InitRequest request;
    private InitResponse response;
    private final long ENGINE_RUNTIME = 60 * 1000;
    private final long SLEEP_TIME = 1 * 1000;
    private final CoreService service = new CoreService();
    private final EngineFuelRegulator regulator = new EngineFuelRegulator();

    public EngineController (InitRequest request) {
        this.request = request;
    }

    public void StartEngine() throws InterruptedException {
        response = service.startEngine(request);

        if (response.getStatus().equals("OK")) {
            runEngine();
        }

        System.out.println("<<<ENGINE STOPPED>>>");
    }


    private void runEngine() throws InterruptedException {
        long t = System.currentTimeMillis();
        long end = t + ENGINE_RUNTIME;
        EngineData engineData;
        EngineRequest request;
        while(System.currentTimeMillis() < end) {
            engineData = service.getEngineStatus(response.getAuthorizationCode());
            System.out.println("--------------");
            System.out.println(engineData);
            request = regulator.getRequest(engineData);

            if (request.getAdjustmentRequest().getValue() != 0 && request.getAdditive() != Additive.NONE) {
                request.getAdjustmentRequest().setAuthorizationCode(response.getAuthorizationCode());
                System.out.println(request);
                service.postAdjustment(request);
            }

            Thread.sleep(SLEEP_TIME);
        }
    }
}
