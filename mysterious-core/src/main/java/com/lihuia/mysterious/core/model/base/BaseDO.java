package com.lihuia.mysterious.core.model.base;

import java.io.Serializable;

/**
 * @author lihuia.com
 * @date 2022/3/27 8:06 PM
 */

public class BaseDO<P> implements Serializable {

    private static final long serialVersionUID = 8702872432337844438L;
    private P id;
    private String creatorId;
    private String creator;
    private String modifierId;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
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
