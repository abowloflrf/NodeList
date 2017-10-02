package cn.lltw.nodelist.Model;


/**
 * Created by ruofeng on 2017/10/1.
 */

public class NodeList {
    private String key;
    private String name;
    private String describe;

    public NodeList(String name, String describe, String key) {
        this.name = name;
        this.describe = describe;
        this.key=key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getKey() {
        return key;
    }
}
