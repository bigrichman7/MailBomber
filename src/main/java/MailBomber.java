import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MailBomber {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Properties props = new Properties();
        File jarPath = new File(MailBomber.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String propertiesPath = jarPath.getParentFile().getAbsolutePath();
        FileInputStream fis = new FileInputStream(propertiesPath + File.separator + "config.properties");
        props.load(fis);

        Generator generator = new Generator(props);
        String[] fishSubjectArray = generator.fishSubjectArray;
        String[] fishTextArray = generator.fishTextArray;

        for (int i = 0; i < Integer.parseInt(props.getProperty("count.messages")); i++) {
            System.out.println("Sending " + (i+1) + "/" + Integer.parseInt(props.getProperty("count.messages")));
            SendMail sm = new SendMail(props, fishSubjectArray[i], fishTextArray[i]);
            Thread.sleep(Integer.parseInt(props.getProperty("time.sleep")));
            System.out.println("Done!");
        }
    }
}
