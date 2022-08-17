package com.vladi.karasove.smartravel.serverObjects;

import java.util.Date;
import java.util.Map;

public class ActivityBoundary {
    private DomainWithId activityId;
    private String type;
    private Instance instance;
    private Date createdTimestamp;
    private InvokedBy invokedBy;
    private Map<String, Object> activityAttributes;

    public ActivityBoundary() {
        super();
    }

    public ActivityBoundary(DomainWithId acticityId) {
        super();
        this.activityId = acticityId;
    }

    public DomainWithId getActicityId() {
        return activityId;
    }

    public void setActicityId(DomainWithId acticityId) {
        this.activityId = acticityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getActivityAttributes() {
        return activityAttributes;
    }

    public void setActivityAttributes(Map<String, Object> activityAttributes) {
        this.activityAttributes = activityAttributes;
    }

    @Override
    public String toString() {
        return "ActivityBoundary [acticityId=" + activityId + ", type=" + type + ", instance=" + instance
                + ", createdTimestamp=" + createdTimestamp + ", invokedBy=" + invokedBy + ", activityAttributes="
                + activityAttributes + "]";
    }

}