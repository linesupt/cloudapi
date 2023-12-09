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

public class DeleteSmsSignResponse extends AbstractModel {

    /**
    * 删除签名响应
    */
    @SerializedName("DeleteSignStatus")
    @Expose
    private DeleteSignStatus DeleteSignStatus;

    /**
    * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
    */
    @SerializedName("RequestId")
    @Expose
    private String RequestId;

    /**
     * Get 删除签名响应 
     * @return DeleteSignStatus 删除签名响应
     */
    public DeleteSignStatus getDeleteSignStatus() {
        return this.DeleteSignStatus;
    }

    /**
     * Set 删除签名响应
     * @param DeleteSignStatus 删除签名响应
     */
    public void setDeleteSignStatus(DeleteSignStatus DeleteSignStatus) {
        this.DeleteSignStatus = DeleteSignStatus;
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

    public DeleteSmsSignResponse() {
    }

    /**
     * NOTE: Any ambiguous key set via .set("AnyKey", "value") will be a shallow copy,
     *       and any explicit key, i.e Foo, set via .setFoo("value") will be a deep copy.
     */
    public DeleteSmsSignResponse(DeleteSmsSignResponse source) {
        if (source.DeleteSignStatus != null) {
            this.DeleteSignStatus = new DeleteSignStatus(source.DeleteSignStatus);
        }
        if (source.RequestId != null) {
            this.RequestId = new String(source.RequestId);
        }
    }


    /**
     * Internal implementation, normal users should not use it.
     */
    public void toMap(HashMap<String, String> map, String prefix) {
        this.setParamObj(map, prefix + "DeleteSignStatus.", this.DeleteSignStatus);
        this.setParamSimple(map, prefix + "RequestId", this.RequestId);

    }
}

