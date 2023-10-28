package com.lineying.entity;

/**
 * 菜单流程
 *
 * @author ganjing
 */
public class MenuProcessEntity {

    private String id;
    private String mid;// '菜单id',
    private String pcontent;// '流程',
    private String pic;// '图片',
    private String create_time;// '创建时间',
    private String update_time;// '修改时间',
    private String pic_isEmpty;

    public String getPic_isEmpty() {
        return pic_isEmpty;
    }

    public void setPic_isEmpty(String pic_isEmpty) {
        this.pic_isEmpty = pic_isEmpty;
    }

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

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
                .append(pcontent).append(separatorStr)
                .append(pic).append(separatorStr)
                .append(create_time).append(separatorStr)
                .append(update_time).append(separatorStr)
                .append(pic_isEmpty);
        return sb.toString();
    }

}
