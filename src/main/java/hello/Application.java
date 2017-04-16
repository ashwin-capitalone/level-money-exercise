package hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    private static final String LEVEL_LABS_ALL_TRANS_URL = 
            "https://2016.api.levelmoney.com/api/v2/core/get-all-transactions";
    
    private static final String LEVEL_LABS_PROJECTED_TRANS_URL = 
            "https://2016.api.levelmoney.com/api/v2/core/projected-transactions-for-month";
    
    private static final String IGNORE_DONUTS_PROP = "ignore-donuts";
    private static final String CRYSTAL_BALL_PROP = "crystal-ball";
    private static final String IGNORE_CC_PAYMENTS = "ignore-cc-payments";
    
    private static final String ALL_TRANS_REQUEST_BODY = 
            "{\"args\": {\"uid\": 1110590645, \"token\": \"528A495056CDBBFDF793D8F150C3117C\", \"api-token\": \"AppTokenForInterview\", \"json-strict-mode\": false, \"json-verbose-response\": false}}";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(ALL_TRANS_REQUEST_BODY, requestHeaders);
            
            LevelLabsResponse levelLabsResponse = restTemplate
                      .postForObject(LEVEL_LABS_ALL_TRANS_URL, entity, LevelLabsResponse.class);
            
            List<Transaction> tList = new ArrayList<>();
            
            if (levelLabsResponse.getError().equalsIgnoreCase("no-error")) {
                tList.addAll(Arrays.asList(levelLabsResponse.getTransactions()));
            }
            
            if (System.getProperties().containsKey(CRYSTAL_BALL_PROP)) {
                log.info("CRYSTAL_BALL: true");
                getCrystalBallTransactionsForCurrentMonth(restTemplate, requestHeaders, tList);
            }

            printMonthlyTransactionsReport(tList);

        };
    }

    private void getCrystalBallTransactionsForCurrentMonth(
            RestTemplate restTemplate, 
            HttpHeaders requestHeaders,
            List<Transaction> tList) {
        
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = 1 + c.get(Calendar.MONTH);
        
        String jsonSuffix = ", \"year\":" + year + ", \"month\":" + month + "}";
        String ignoreCCRequestBody = ALL_TRANS_REQUEST_BODY
                .substring(0, ALL_TRANS_REQUEST_BODY.lastIndexOf("}")) + jsonSuffix;
        
        log.info("Request body for projected transactions endpoint: " + ignoreCCRequestBody);
        HttpEntity<String> entity = new HttpEntity<String>(ignoreCCRequestBody, requestHeaders);
        
        LevelLabsResponse levelLabsResponse = restTemplate.postForObject(LEVEL_LABS_PROJECTED_TRANS_URL, entity, LevelLabsResponse.class);

        if (levelLabsResponse.getError().equalsIgnoreCase("no-error")) {
            log.info("Crystal ball API returned {} transactions", levelLabsResponse.getTransactions().length);    
            tList.addAll(Arrays.asList(levelLabsResponse.getTransactions()));
        } else {
            log.error("Crystal ball API returned an error");
        }
    }

    private void printMonthlyTransactionsReport(List<Transaction> transactions) {
        
        Map<String, List<Transaction>> map = getAllTransactionsMap(transactions);

        log.info(System.lineSeparator());
        log.info("++++++++++   SPENDING SUMMARY BY MONTH   +++++++++++");
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        printTransactions(map);
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.info(System.lineSeparator());
        
    }

    @SuppressWarnings("unchecked")
    private void printTransactions(Map<String, List<Transaction>> transMap) {
        // Iterate over the map for each month and print the transaction report
        for (String dateKey : transMap.keySet()) {
            
            long spentAmount = 0;
            long incomeAmt = 0;
            for (Transaction trans : transMap.get(dateKey)) {
                if (trans.getAmount() < 0) {
                    spentAmount += Math.abs(trans.getAmount());
                } else {
                    incomeAmt += trans.getAmount();
                }
            }
            
            JSONObject monthlySummary = new JSONObject();
            monthlySummary.put("spent", "$" + String.valueOf(((double)spentAmount)/10_000));
            monthlySummary.put("income", "$" + String.valueOf(((double)incomeAmt)/10_000));

            JSONObject monthlyReport = new JSONObject();
            monthlyReport.put(dateKey, monthlySummary);
            log.info(monthlyReport.toJSONString());
            
        }
    }

    private Map<String, List<Transaction>> getAllTransactionsMap(List<Transaction> transactions) {
        Map<String, List<Transaction>> map = new TreeMap<>();
        
        final boolean ignoreDonuts = System.getProperties().containsKey(IGNORE_DONUTS_PROP);
        
        log.info("IGNORE_DONUTS: {}", ignoreDonuts);
        
        // Create the map
        for (Transaction trans : transactions) {
            
            if (ignoreDonuts) {
                String merchantName = trans.getMerchant().toLowerCase();
                if (merchantName.contains("krispy kreme donuts") || merchantName.contains("dunkin")) {
                    continue;
                }
            }

            String transTime = trans.getTransactionTime();
            String dateKey = transTime.substring(0, 7);

            if (map.containsKey(dateKey)) {
                map.get(dateKey).add(trans);
            } else {
                List<Transaction> values = new ArrayList<>();
                values.add(trans);
                map.put(dateKey, values);
            }
            
        }
        return map;
    }

}