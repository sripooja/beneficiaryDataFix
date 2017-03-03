import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private InputStream inputStream;
    private Properties properties;

    public ConfigReader(String configFilePath) {
        block3 : {
            this.properties = new Properties();
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            try {
                this.inputStream = new FileInputStream(configFilePath);
                if (this.inputStream != null) {
                    this.properties.load(this.inputStream);
                    break block3;
                }
                throw new FileNotFoundException("No property file with name " + configFilePath + " found");
            }
            catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    public String getProperty(String propertyName) {
        return this.properties.getProperty(propertyName);
    }
}