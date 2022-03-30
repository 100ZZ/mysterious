package com.lihuia.mysterious.core.vo.base;

/**
 * @author lihuia.com
 * @date 2022/3/29 10:08 PM
 */

public class BaseVO<P> {

    private P id;
    private Long creatorId;
    private String creator;
    private Long modifierId;
    private String modifier;
    private Long createTime;
    private Long modifyTime;

    public P getId() {
        return id;
    }

    public void setId(P id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }
}
