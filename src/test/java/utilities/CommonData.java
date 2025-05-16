package utilities;

import java.io.FileInputStream;
import java.util.Properties;
public class CommonData {
    public static String env;
    private Properties properties;

    public static Properties config;
    public static String baseURL;

    public String getCommonData(String sName) {
        String commonData = properties.getProperty(sName);
        if (commonData != null) return commonData;
        else throw new RuntimeException(sName);
    }

    public static Properties config_reader(String FilePath) {

        try {
            config = new Properties();
            config.load(new FileInputStream(FilePath));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return config;

    }

}
