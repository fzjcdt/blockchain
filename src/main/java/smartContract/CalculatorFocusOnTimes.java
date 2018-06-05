package smartContract;

import java.util.List;

public class CalculatorFocusOnTimes implements Calculator {

    @Override
    public double calculate(List<ReportItem> reportItemList) {
        double rst = 0.0;
        for (ReportItem reportItem : reportItemList) {
            if (reportItem.isRelieve()) {
                rst -= Math.log(reportItem.getValidValue());
            } else {
                rst += Math.log(reportItem.getValidValue());
            }
        }

        return rst;
    }
}
