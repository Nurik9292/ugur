package tm.ugur.util.pojo;

import java.util.List;
import java.util.Map;

public class ImportData {

    private List<Map<String, Map<String, Integer>>> data;

    public ImportData(){}

    public ImportData(List<Map<String, Map<String, Integer>>> data) {
        this.data = data;
    }

    public List<Map<String, Map<String, Integer>>> getData() {
        return data;
    }

    public void setData(List<Map<String, Map<String, Integer>>> data) {
        this.data = data;
    }
}
