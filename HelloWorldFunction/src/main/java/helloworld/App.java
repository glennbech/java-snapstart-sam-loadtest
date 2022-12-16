package helloworld;

import static java.math.BigInteger.ZERO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.crac.Core;
import org.crac.Resource;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, Resource {

    BigInteger arraySum;

    public App() {
        Core.getGlobalContext().register(this);
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        // In a cold start scenario - this will be null, with SnapStart enabled it will always be set
        // Since the beforeCheckpoint method will set it before a SnapShot is taken.
        if  (arraySum == null) {
            arraySum = doSomeWorkPlease();
        }

        String output = String.valueOf(arraySum);
        return response.withStatusCode(200).withBody(output);
    }

    /**
     * This method creates a randomly generated 100 million sized, two-dimensional
     * array with values between 0 and 42, and then sums them. Yeah, I could
     * have made a one dimensional array - But I wanted to play around a bit
     * with Java streams since my coding is rusty :)
     *
     * @return the s'um of the randomly generated Array
     */
    public static BigInteger doSomeWorkPlease() {
        int[][] matrix = new int[10000][10000];
        for (int n = 0; n < matrix[0].length; n++) {
            for (int j = 0; j < matrix[n].length; j++) {
                matrix[n][j] = (int) (Math.random() * 42);
            }
        }
        return Arrays.stream(matrix)
                .flatMapToInt(Arrays::stream)
                .mapToObj(BigInteger::valueOf)
                .reduce(ZERO, BigInteger::add);
    }

    @Override
    public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
        arraySum = doSomeWorkPlease();
    }

    @Override
    public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
    }
}