package com.dyzhsw.efficient.dto;

import java.util.List;

/**
 * @Author: pjx
 * @Date: 2018/12/11 10:52
 * @Version 1.0
 */
public class TreeEntity<T> {
    private static final long serialVersionUID = 1L;

    /**
     * varchar(64) NULL父id
     */

    protected String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public T getParentTree() {
        return parentTree;
    }

    public void setParentTree(T parentTree) {
        this.parentTree = parentTree;
    }

    /**
     * 节点层次（第一层，第二层，第三层....）
     */
    protected Integer level;
    /**
     * varchar(2000) NULL路径
     */
    protected String parentIds;
    /**
     * int(11) NULL排序
     */
    protected Integer sort;

    protected List<T> children;

    protected T parentTree;

    public TreeEntity() {
        super();
        this.sort = 30;
    }
}
