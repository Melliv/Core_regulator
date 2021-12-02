package Services;

import Domain.EngineData;
import Domain.EngineRequest;
import Domain.InitRequest;
import Domain.InitResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class CoreService {

    private static final String BASE_URL = "https://warp-regulator-bd7q33crqa-lz.a.run.app/api/";
    private HttpURLConnection connection;
    private final ObjectMapper MAPPER = new ObjectMapper();
    private static final String START_API_END_POINT = "start";
    private static final String ENGINE_STATUS_API_END_POINT = "status";
    private static final String ADJUST_API_END_POINT = "adjust";

    public InitResponse startEngine(InitRequest requestBody) {
        InitResponse responseBody = null;
        try {
            URL url = new URL(BASE_URL + START_API_END_POINT);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = MAPPER.writeValueAsBytes(requestBody);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseBody = MAPPER.readValue(response.toString(), InitResponse.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return responseBody;
    }

    public EngineData getEngineStatus(String authorizationCode) {
        EngineData responseBody = null;
        try {
            URL url = new URL(BASE_URL + ENGINE_STATUS_API_END_POINT + getURLAuthorizationCode(authorizationCode));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setDoOutput(true);

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseBody = MAPPER.readValue(response.toString(), EngineData.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return responseBody;
    }

    public void postAdjustment(EngineRequest engineRequest) {
        try {
            URL url = new URL(BASE_URL + ADJUST_API_END_POINT + "/" + engineRequest.getAdditive().name());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = MAPPER.writeValueAsBytes(engineRequest.getAdjustmentRequest());
                os.write(input, 0, input.length);
            }
            System.out.println(connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    private String getURLAuthorizationCode(String authorizationCode) {
        return String.format("?authorizationCode=%s", authorizationCode);
    }
}
