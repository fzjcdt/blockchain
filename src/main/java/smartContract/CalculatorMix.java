package smartContract;

import java.util.List;

public class CalculatorMix implements Calculator {

    @Override
    public double calculate(List<ReportItem> reportItemList) {
        double rst = 0.0;
        for (ReportItem reportItem : reportItemList) {
            if (reportItem.isRelieve()) {
                rst = rst - reportItem.getValidValue() - Math.log(reportItem.getValidValue());
            } else {
                rst = rst + reportItem.getValidValue() + Math.log(reportItem.getValidValue());
            }
        }

        return rst;
    }
}
