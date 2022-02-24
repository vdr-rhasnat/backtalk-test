package Context;

import GenericInfo.genericFactory;

import java.sql.Connection;
import java.util.ArrayList;

public class GlobalContext {
    //Database Status
    public static Connection connection;
    public static String connectionStatus;

    //Other Variable
    public static ArrayList<String[]> statusDetails = new ArrayList<>();
    public static int checked = 0;
    public static int checkedBy = 0;
    public static int XMLGenerated = 0;
    public static int CVSAcknowlogement = 0;
    public static Integer UpdateStatusID = 0;
    public static String orderId = "";
    public static Integer BagNumber = 0;
    public static String fileName = "";
    public static genericFactory gen = new genericFactory();

}
