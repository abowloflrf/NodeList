package cn.lltw.nodelist.Model;

/**
 * Created by ruofeng on 2017/10/1.
 */

public class NodeTask {
    private boolean complete;
    private String key;
    private String name;
    private String repeat;
    private String dueTime;
    private String describe;

    public NodeTask(String name, String describe, String key) {
        this.name = name;
        this.key = key;
        this.describe = describe;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
