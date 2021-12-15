/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.dashboard.dingding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉机器人上传
 */
@Slf4j
public class DingTalkExportHandler {
    /**
     * 钉钉WEB_HOOK地址
     */
    private static final String PREFIX_WEB_HOOK = "https://oapi.dingtalk.com/robot/send?access_token=";

    public static void export(String message) {
        if (message == null) {
            return;
        }

        try {
            // 钉钉消息不能发送过快，一分钟最多二十次
            Thread.sleep(3000);

            String token = PREFIX_WEB_HOOK + TokenFetcher.resolveToken(null);
            DingTalkClient client = new DefaultDingTalkClient(token);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(message);
            request.setText(text);

            OapiRobotSendResponse response = client.execute(request);
            log.info(response.getErrmsg());
        } catch (Exception e) {
            log.error("发送消息到钉钉失败，原始消息：{}，原因：{}", message, e);
        }
    }
}
