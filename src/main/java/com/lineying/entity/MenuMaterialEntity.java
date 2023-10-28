package com.lineying.entity;

/**
 * 菜单素材类
 *
 * @author ganjing
 */
public class MenuMaterialEntity {

    private String id;
    private String mid;// '菜单信息表id',
    private String mname;// '调料名称',
    private String type;// '调料类型',
    private String amount;// '用量',
    private String create_time;// '创建时间',
    private String update_time;// '最后一次修改时间',

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        String separatorStr = " - ";
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(separatorStr)
                .append(mid).append(separatorStr)
                .append(mname).append(separatorStr)
                .append(type).append(separatorStr)
                .append(amount).append(separatorStr)
                .append(create_time).append(separatorStr)
                .append(update_time);
        return sb.toString();
    }

}
