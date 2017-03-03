import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Provide file Path as Argument.");
            return;
        }
        System.out.println("File Path provided is " + args[0]);
        ConfigReader configReader = new ConfigReader(args[0]);
        String DB_DRIVER = configReader.getProperty("DB_DRIVER");
        System.out.println(DB_DRIVER);
        String DB_CONNECTION = configReader.getProperty("DB_CONNECTION");
        System.out.println(DB_CONNECTION);
        String DB_USER = configReader.getProperty("DB_USER");
        System.out.println(DB_USER);
        String DB_PASSWORD = configReader.getProperty("DB_PASSWORD");
        System.out.println(DB_PASSWORD);
        String csvPath = configReader.getProperty("CSVPATH");
        System.out.println(csvPath);

        String line = "";
        String csvSplitBy = ",";

        int notFoundCount = 0;
        int subscriberPurgedCount = 0;

        Map<String, List<Object>> dbList = dbConnection.getAllIndependentChildRecords(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            while ((line = br.readLine()) != null) {
                String[] csvlineList = line.split(csvSplitBy);
                UpdateMotherSubscriber.updateSubscriber(csvlineList, dbList, subscriberPurgedCount, notFoundCount);
            }
            System.out.println("Count of Subscribers not found in Database " + notFoundCount);
            System.out.println("Count of Subscribers purged " + subscriberPurgedCount);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}