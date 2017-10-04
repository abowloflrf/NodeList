package cn.lltw.nodelist.Model;

/**
 * Created by ruofeng on 2017/10/1.
 */

public class NodeTask {
    private boolean complete;
    private String name;
    private String repeat;
    private String dueTime;
    private String remark;

    public NodeTask(String name) {
        this.name = name;
    }

    public NodeTask(String name, String remark) {
        this(name);
        this.remark = remark;
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

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
