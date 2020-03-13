import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class Generator {
    String fishURLSubjects;
    String fishURLParagraphs;
    URL obj;
    HttpURLConnection connection;
    String[] fishSubjectArray;
    String[] fishTextArray;
    int numberParagraphs;

    public Generator(Properties props) throws IOException, ParseException {
        numberParagraphs = Integer.parseInt(props.getProperty("count.messages"));
        fishSubjectArray = new String[numberParagraphs];
        fishTextArray = new String[numberParagraphs];
        fishURLSubjects = "https://fish-text.ru/get?&type=title&number=" + numberParagraphs;
        fishURLParagraphs = "https://fish-text.ru/get?&type=paragraph&number=" + numberParagraphs;
        obj = new URL(fishURLSubjects);
        connection = (HttpURLConnection) obj.openConnection();
        JSONParserSubjects(getResponse());
        obj = new URL(fishURLParagraphs);
        connection = (HttpURLConnection) obj.openConnection();
        JSONParserParagraphs(getResponse());
    }

    private StringBuffer getResponse() throws IOException {
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

    private void JSONParserSubjects(StringBuffer response) throws ParseException {
        Object obj = new JSONParser().parse(response.toString());
        JSONObject jo = (JSONObject) obj;
        String joStr;
        if (jo.get("status").equals("success")) {
            joStr = jo.get("text").toString();
            fishSubjectArray = joStr.split("\\\\n\\\\n");
        } else System.out.println("Error getting JSON object from: " + fishURLSubjects);
    }

    private void JSONParserParagraphs(StringBuffer response) throws ParseException {
        Object obj = new JSONParser().parse(response.toString());
        JSONObject jo = (JSONObject) obj;
        String joStr;
        if (jo.get("status").equals("success")) {
            joStr = jo.get("text").toString();
            fishTextArray = joStr.split("\\\\n\\\\n");
        } else System.out.println("Error getting JSON object from: " + fishURLParagraphs);
    }
}
