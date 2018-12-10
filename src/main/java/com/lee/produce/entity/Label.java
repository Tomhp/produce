package com.shipparts.console.entity;

import java.util.Date;

/**
* @Description: 标签实体类
* @Author: Lee
* @DateTime: 2018/11/30 9:38
*/
public class Label {
    /**
     * 标签id
     */
    private Integer labelId;
    /**
     * 标签名
     */
    private String name;
    /**
     * 中文名
     */
    private String CnName;
    /**
     * 排序值
     */
    private Integer sortIndex;
    /**
     *  是否删除
     */
    private Integer isDelete;
    /**
     * 插入时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private  Date updateTime;

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return CnName;
    }

    public void setCnName(String cnName) {
        CnName = cnName;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
