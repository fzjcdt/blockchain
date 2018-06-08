package smartContract;

import static transaction.TransactionUtil.sendFunds;

public class Report {

    public static synchronized boolean reportBlackList(String sender, String priKey, double value, String ip) {
        return sendFunds(sender, priKey, "", value, ip, false);
    }

    public static synchronized boolean reportBlackList(String sender, String priKey, double value, String ip, boolean relieve) {
        return sendFunds(sender, priKey, "", value, ip, relieve);
    }
}
