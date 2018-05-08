package log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

    private static Calendar now = Calendar.getInstance();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String LOG_FOLDER_NAME = "log";

    private static final String LOG_FILE_SUFFIX = ".log";

    private static MyLogFormatter myLogFormatter = new MyLogFormatter();

    private static FileHandler fileHandler = getFileHandler();

    private static Logger logger = setLoggerHanlder();

    private static boolean logSwitch = true;

    private volatile static LogUtil instance;

    public static LogUtil getInstance() {
        if (instance == null) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        return instance;
    }

    private synchronized static String getLogFilePath() {
        StringBuffer logFilePath = new StringBuffer();

        File proDir = new File("");
        try {
            logFilePath.append(proDir.getAbsolutePath());
        } catch (Exception e) {
        }

        logFilePath.append(File.separatorChar);
        logFilePath.append(LOG_FOLDER_NAME);

        File dir = new File(logFilePath.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        logFilePath.append(File.separatorChar);
        logFilePath.append(sdf.format(new Date()));
        logFilePath.append(LOG_FILE_SUFFIX);

        return logFilePath.toString();
    }

    private static FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        boolean APPEND_MODE = true;
        try {
            //文件日志内容标记为可追加
            fileHandler = new FileHandler(getLogFilePath(), APPEND_MODE);
            return fileHandler;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // SEVERE > WARNING > INFO > CONFIG > FINE > FINER > FINESET
    public synchronized static Logger setLoggerHanlder() {
        Logger logger = Logger.getLogger("Blockchain log");
        try {
            fileHandler.setFormatter(myLogFormatter);
            logger.addHandler(fileHandler);
        } catch (SecurityException e) {
            logger.severe(populateExceptionStackTrace(e));
        }

        return logger;
    }

    private synchronized static String populateExceptionStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (StackTraceElement elem : e.getStackTrace()) {
            sb.append("\tat ").append(elem).append("\n");
        }
        return sb.toString();
    }

    public synchronized static void Log(Level level, String msg) {
        if (logSwitch) {
            logger.log(level, msg);
        }
    }

    public static void turnOff() {
        logSwitch = false;
    }

    public static void turnOn() {
        logSwitch = true;
    }

    public static void main(String[] args) {
        LogUtil.Log(Level.FINE, "fine");
        LogUtil.Log(Level.INFO, "sdaf");
        LogUtil.Log(Level.CONFIG, "config");
        LogUtil.Log(Level.SEVERE, "config");
    }
}