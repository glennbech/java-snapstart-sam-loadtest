package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static java.math.BigInteger.ZERO;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        String output = String.valueOf(doSomeWorkPlease());
        return response.withStatusCode(200).withBody(output);
    }

    /**
     * Creates a randomly generated 100,000,0000 sized two-dimensional array with values between 0 and 42, and then sums them
     *
     * @return the sum of the randomly generated Array
     */
    public static BigInteger doSomeWorkPlease() {
        int[][] matrix = new int[10000][10000];
        for (int n = 0; n < matrix[0].length; n++) {
            for (int j = 0; j < matrix[n].length; j++) {
                matrix[n][j] = (int) (Math.random() * 42);
            }
        }
        return Arrays.stream(matrix).flatMapToInt(Arrays::stream).mapToObj(BigInteger::valueOf).reduce(ZERO, BigInteger::add);
    }
}