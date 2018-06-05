package smartContract;

import java.util.List;

public class CalculatorFocusOnValue implements Calculator {

    @Override
    public double calculate(List<ReportItem> reportItemList) {
        double rst = 0.0;
        for (ReportItem reportItem : reportItemList) {
            if (reportItem.isRelieve()) {
                rst -= reportItem.getValidValue();
            } else {
                rst += reportItem.getValidValue();
            }
        }

        return (double) Math.round(rst * 100) / 100;
    }
}
