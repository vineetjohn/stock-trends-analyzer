package ca.uwaterloo.stock_trends_analyzer.utils;

import ca.uwaterloo.stock_trends_analyzer.constants.Constants;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class StockQueryHelper
{
    private static Logger log = LogManager.getLogger(StockQueryHelper.class);

    public static void getStockHistory(
        String stockSymbol, Integer startMonth, Integer startDay, Integer startYear,
        Integer endMonth, Integer endDay, Integer endYear, String outputDirectory
    )
        throws IOException
    {
        StrBuilder strBuilder = new StrBuilder(Constants.STOCK_PRICE_ENDPOINT);
        strBuilder.replaceAll(Constants.SYMBOL_PLACEHOLDER, stockSymbol);
        strBuilder.replaceAll(Constants.START_MONTH_PLACEHOLDER, String.valueOf(startMonth));
        strBuilder.replaceAll(Constants.START_DAY_PLACEHOLDER, String.valueOf(startDay));
        strBuilder.replaceAll(Constants.START_YEAR_PLACEHOLDER, String.valueOf(startYear));
        strBuilder.replaceAll(Constants.END_MONTH_PLACEHOLDER, String.valueOf(endMonth));
        strBuilder.replaceAll(Constants.END_DAY_PLACEHOLDER, String.valueOf(endDay));
        strBuilder.replaceAll(Constants.END_YEAR_PLACEHOLDER, String.valueOf(endYear));

        String endpoint = strBuilder.toString();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(endpoint);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        String filepath = outputDirectory + Constants.OUTPUT_FILE_PREFIX + "_" + stockSymbol;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));

        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
    }


}
