package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.MerchantModel;
import play.mvc.*;
import play.libs.Json;
import java.util.*;

public class UserController extends Controller{

    private HashMap<Integer, MerchantModel> userMap = new HashMap<>();
    private final int threshold = 3;

    public Result add(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        Transaction transaction = Json.fromJson(jsonNode, Transaction.class);
        MerchantModel merchantModel = userMap.getOrDefault(transaction.getUserId(), new MerchantModel());
        if(!userMap.containsKey(transaction.getUserId())) {
            userMap.put(transaction.getUserId(), merchantModel);
        }
        HashMap<String, Merchant> merchantMap = merchantModel.merchantMap;
        Merchant merchant = merchantMap.getOrDefault(transaction.getMerchantName(), new Merchant(transaction.getMerchantName(), 0));
        merchant.incrementVisited();
        merchantMap.put(transaction.getMerchantName(), merchant);
        insertInMerchantHeap(merchant, merchantModel.merchantMaxHeap);
        return ok("Inserted");
    }

    public void insertInMerchantHeap(Merchant merchant, PriorityQueue heap) {
        if (heap.contains(merchant)) {
            /**
             * Since merchant count is incremented original priorityQueue must be updated inorder to return latest max
              */

            heap.remove(merchant);
            heap.add(merchant);
        } else if (heap.size() < threshold) {
            heap.add(merchant);
        } else {
            /**
             * We will compare merchant heap starting from lowest visisted node.
             * Since priority is maintained in max heap smallest element will be in threshold - i position.
             */
            int i = threshold-1;
            Iterator<Merchant> merchantIterator = heap.iterator();
            while (i>-1 && merchantIterator.hasNext()) {
                Merchant tempMerchant = merchantIterator.next();
                if(tempMerchant.getVisitedCount() < merchant.getVisitedCount()) {
                    heap.remove(tempMerchant);
                    heap.add(merchant);
                    return;
                }
            }
        }
    }

    public JsonNode getMostVisited(int userId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userDetail = mapper.createObjectNode();
        userDetail.put("userId", userId);
        MerchantModel merchantModel = userMap.get(userId);
        if (merchantModel == null || merchantModel.merchantMaxHeap.size()<3)
            userDetail.put("mostVisistedMerchants", "Error - Too few transactions");
        else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Merchant merchant : merchantModel.merchantMaxHeap) {
                stringBuilder.append(merchant.getName());
                stringBuilder.append(", ");
            }
            userDetail.put("mostVisistedMerchants", stringBuilder.toString());;
        }
        return Json.toJson(userDetail);
    }

    public Result getAll() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        for (Map.Entry<Integer, MerchantModel> entry : userMap.entrySet()) {
            Integer userId = entry.getKey();
            JsonNode merchant = getMostVisited(userId);
            result.put(userId.toString(), Json.prettyPrint(Json.toJson(merchant)));
        }
        System.out.println(String.format("Expected %s", Json.prettyPrint(Json.toJson(result))));
        System.out.println(String.format("All data %s", Json.prettyPrint(Json.toJson(userMap))));
        return ok(Json.toJson(result));
    }
}
