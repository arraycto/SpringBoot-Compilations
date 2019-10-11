package com.wjwcloud.ad.client.response.base;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BaseActivityPartResponse
 */
public abstract class BaseActivityPartResponse implements ActivityResponse {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
