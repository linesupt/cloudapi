package com.lineying.entity;

import java.util.List;

/**
 * @author ganjing
 */
public class MenuClassEntity {

    private String id;
    private String name;
    private String classId;
    private String parentid;
    private List<MenuClassEntity> list;

    public List<MenuClassEntity> getList() {
        return list;
    }

    public void setList(List<MenuClassEntity> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getclassId() {
        return classId;
    }

    public void setclassId(String classId) {
        this.classId = classId;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    @Override
    public String toString() {
        String separatorStr = " - ";
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(separatorStr)
                .append(name).append(separatorStr)
                .append(classId).append(separatorStr)
                .append(parentid);
        return sb.toString();
    }

}
