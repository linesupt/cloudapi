/*
 * Copyright (c) 2017-2018 THL A29 Limited, a Tencent company. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lineying.sms.tencentcloud.models;

import com.lineying.sms.tencentcloud.common.AbstractModel;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import java.util.HashMap;

public class DescribeSmsSignListResponse extends AbstractModel {

    /**
    * 获取签名信息响应
    */
    @SerializedName("DescribeSignListStatusSet")
    @Expose
    private DescribeSignListStatus [] DescribeSignListStatusSet;

    /**
    * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
    */
    @SerializedName("RequestId")
    @Expose
    private String RequestId;

    /**
     * Get 获取签名信息响应 
     * @return DescribeSignListStatusSet 获取签名信息响应
     */
    public DescribeSignListStatus [] getDescribeSignListStatusSet() {
        return this.DescribeSignListStatusSet;
    }

    /**
     * Set 获取签名信息响应
     * @param DescribeSignListStatusSet 获取签名信息响应
     */
    public void setDescribeSignListStatusSet(DescribeSignListStatus [] DescribeSignListStatusSet) {
        this.DescribeSignListStatusSet = DescribeSignListStatusSet;
    }

    /**
     * Get 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。 
     * @return RequestId 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public String getRequestId() {
        return this.RequestId;
    }

    /**
     * Set 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     * @param RequestId 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }

    public DescribeSmsSignListResponse() {
    }

    /**
     * NOTE: Any ambiguous key set via .set("AnyKey", "value") will be a shallow copy,
     *       and any explicit key, i.e Foo, set via .setFoo("value") will be a deep copy.
     */
    public DescribeSmsSignListResponse(DescribeSmsSignListResponse source) {
        if (source.DescribeSignListStatusSet != null) {
            this.DescribeSignListStatusSet = new DescribeSignListStatus[source.DescribeSignListStatusSet.length];
            for (int i = 0; i < source.DescribeSignListStatusSet.length; i++) {
                this.DescribeSignListStatusSet[i] = new DescribeSignListStatus(source.DescribeSignListStatusSet[i]);
            }
        }
        if (source.RequestId != null) {
            this.RequestId = new String(source.RequestId);
        }
    }


    /**
     * Internal implementation, normal users should not use it.
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamArrayObj(map, prefix + "DescribeSignListStatusSet.", this.DescribeSignListStatusSet);
        this.setParamSimple(map, prefix + "RequestId", this.RequestId);

    }
}

