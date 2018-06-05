package smartContract;

import java.util.ArrayList;
import java.util.List;

public class BlackListRst {
    private String ip;
    private List<Double> value;

    public BlackListRst(String ip) {
        setIp(ip);
        setValue(new ArrayList<Double>());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Double> getValue() {
        return value;
    }

    public void setValue(List<Double> value) {
        this.value = value;
    }

    public void addValue(double v) {
        this.value.add(v);
    }

    public String toString() {
        return ip + ": " + value.toString();
    }
}
