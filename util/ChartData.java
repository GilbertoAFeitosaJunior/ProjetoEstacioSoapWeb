package mobi.stos.projetoestacio.util;

import java.util.List;

public class ChartData {

    private Object key;
    private Object value;
    private List list;

    public ChartData() {
    }

    public ChartData(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

}
