package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class UserControllerTest extends WithApplication {

    private static final String GET_URI = "/getAll";
    private static final String POST_URI = "/add";
    private static final String transactionFilePath = new File("test/resources/coding_challenge_data.csv")
            .getAbsolutePath();
    private static final Http.RequestBuilder getRequest = new Http.RequestBuilder()
            .method(GET)
            .uri(GET_URI);

    private static final Http.RequestBuilder postRequest = new Http.RequestBuilder()
            .method(POST)
            .uri(POST_URI);

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void addTransactions() {
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(transactionFilePath))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            lines.forEach((line) -> {
                String[] transactionDetail = line.split(",");
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode transaction = mapper.createObjectNode();
                transaction.put("userId", transactionDetail[0]);
                transaction.put("merchantName", transactionDetail[1]);
                transaction.put("val1", transactionDetail[2]);
                transaction.put("price", transactionDetail[3]);
                transaction.put("purchaseDate", transactionDetail[4]);
                transaction.put("taxId", transactionDetail[5]);
                Result addResult = route(app, postRequest.bodyJson(transaction));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Result result = route(app, getRequest);
        String output = contentAsString(result);

        // System.out.println(Json.prettyPrint(Json.parse(output)));
        assertEquals(OK, result.status());
    }

}
