package tm.ugur.pojo;

import javax.sound.sampled.Line;
import java.io.Serializable;
import java.util.List;

public class CustomLine implements Serializable {

    List<CustomPoint> line;

    public CustomLine(){}

    public CustomLine(List<CustomPoint> line) {
        this.line = line;
    }

    public List<CustomPoint> getLine() {
        return line;
    }

    public void setLine(List<CustomPoint> line) {
        this.line = line;
    }
}
