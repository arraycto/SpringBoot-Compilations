package com.wjwcloud.tsl.data.kv;

import java.io.Serializable;
import java.util.Optional;

/**
 * @Author:
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface KvEntry extends Serializable {

    String getKey();

    DataType getDataType();

    Optional<String> getStrValue();

    Optional<Long> getLongValue();

    Optional<Boolean> getBooleanValue();

    Optional<Double> getDoubleValue();

    String getValueAsString();

    Object getValue();

}
