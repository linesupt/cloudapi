package com.lineying.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单信息实体类
 *
 * @author ganjing
 */
public class MenuInfoEntity {

    private String id;
    private String sid;// '第三方id',
    private String name;// '菜品名称',
    private String peoplenum;// '适合人数',
    private String classId;// '菜类型',
    private String preparetime;// '准备时间',
    private String cookingtime;// '烹饪所需要时间',
    private String content;// '内容',
    private String pic;// '菜品封面图片',
    private String tag;// '标签',
    private String create_time;// '创建时间',
    private String update_time;// '最后一次修改时间',
    // 需要的食材
    private List<MenuMaterialEntity> materialList = new ArrayList<MenuMaterialEntity>();
    // 处理步骤
    private List<MenuProcessEntity> processList = new ArrayList<MenuProcessEntity>();
    private String pic_isEmpty;

    public String getPic_isEmpty() {
        return pic_isEmpty;
    }

    public void setPic_isEmpty(String pic_isEmpty) {
        this.pic_isEmpty = pic_isEmpty;
    }

    public List<MenuMaterialEntity> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<MenuMaterialEntity> materialList) {
        this.materialList = materialList;
    }

    public List<MenuProcessEntity> getProcessList() {
        return processList;
    }

    public void setProcessList(List<MenuProcessEntity> processList) {
        this.processList = processList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(String peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getclassId() {
        return classId;
    }

    public void setclassId(String classId) {
        this.classId = classId;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public void setPreparetime(String preparetime) {
        this.preparetime = preparetime;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
                .append(sid).append(separatorStr)
                .append(name).append(separatorStr)
                .append(peoplenum).append(separatorStr)
                .append(classId).append(separatorStr)
                .append(preparetime).append(separatorStr)
                .append(cookingtime).append(separatorStr)
                .append(content).append(separatorStr)
                .append(pic).append(separatorStr)
                .append(tag).append(separatorStr)
                .append(create_time).append(separatorStr)
                .append(update_time);
        return sb.toString();
    }
}
