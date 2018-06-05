package smartContract;

import block.Block;
import block.BlockChain;
import transaction.Transaction;
import util.IPUtil;

import java.util.*;

public class SmartContract {
    private static final Calculator[] calculators = {new CalculatorFocusOnTimes(),
            new CalculatorFocusOnValue(), new CalculatorMix()};

    public static List getBlackList() {
        Map<String, List<ReportItem>> reportItems = getAllReportItem();

        return null;
    }

    private static void addReportItemInBlock(Map<String, List<ReportItem>> rst, Block block) {
        if (block.getTransactions() == null) {
            return;
        }

        String data;
        ReportItem reportItem;
        for (Transaction transaction : block.getTransactions()) {
            data = transaction.getData();
            if (IPUtil.isIp(data)) {
                reportItem = new ReportItem(transaction.getValue(),
                        block.getTimeStamp(), transaction.isDataFlag());
                if (rst.containsKey(data)) {
                    rst.get(data).add(reportItem);
                } else {
                    List<ReportItem> reportItemList = new ArrayList<ReportItem>();
                    reportItemList.add(reportItem);
                    rst.put(data, reportItemList);
                }
            }
        }
    }

    private static Map<String, List<ReportItem>> getAllReportItem() {
        Map<String, List<ReportItem>> rst = new HashMap<String, List<ReportItem>>();

        Iterator<Block> it = BlockChain.blockChain.iterator();
        while (it.hasNext()) {
            addReportItemInBlock(rst, it.next());
        }

        return rst;
    }

    public static void main(String[] args) {
        ReportItem r = new ReportItem(100, Long.valueOf("1528208249046"), false);
        List<ReportItem> list = new ArrayList<ReportItem>();
        list.add(r);

        for (Calculator c : calculators) {
            System.out.println(c.calculate(list));
        }
    }
}
