package smartContract;

import java.util.Date;

public class ReportItem {
    private double value;
    private double validValue;
    private long reportTime;
    private boolean relieve;

    private static final int milSecondOneWeek = 1000 * 60 * 60 * 24 * 7;

    public ReportItem(double value, long reportTime) {
        this(value, reportTime, false);
    }

    public ReportItem(double value, long reportTime, boolean isRelieve) {
        setValue(value);
        setReportTime(reportTime);
        setRelieve(isRelieve);
        setValidValue();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public boolean isRelieve() {
        return relieve;
    }

    public void setRelieve(boolean relieve) {
        this.relieve = relieve;
    }

    public void setValidValue() {
        long interval = new Date().getTime() - reportTime;
        double tempValue = value * milSecondOneWeek / (interval + milSecondOneWeek);
        this.validValue = tempValue > 1.0 ? tempValue : 1.0;
    }

    public double getValidValue() {
        return validValue;
    }
}
