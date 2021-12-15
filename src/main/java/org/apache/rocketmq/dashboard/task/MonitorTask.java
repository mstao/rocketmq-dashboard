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
package org.apache.rocketmq.dashboard.task;

import org.apache.rocketmq.dashboard.dingding.DingTalkExportHandler;
import org.apache.rocketmq.dashboard.model.ConsumerMonitorConfig;
import org.apache.rocketmq.dashboard.model.GroupConsumeInfo;
import org.apache.rocketmq.dashboard.service.ConsumerService;
import org.apache.rocketmq.dashboard.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class MonitorTask {
    private static final String PREFIX_CONSUMER_DOWN = "[MQ预警] - 消费者下线";
    private static final String PREFIX_MESSAGE_STACKING = "[MQ预警] - 消息堆积";

    private Logger logger = LoggerFactory.getLogger(MonitorTask.class);

    @Resource
    private MonitorService monitorService;

    @Resource
    private ConsumerService consumerService;

    @Scheduled(cron = "0 */2 * * * ?")
    public void scanProblemConsumeGroup() {
        for (Map.Entry<String, ConsumerMonitorConfig> configEntry : monitorService.queryConsumerMonitorConfig().entrySet()) {
            GroupConsumeInfo consumeInfo = consumerService.queryGroup(configEntry.getKey());
            if (consumeInfo.getCount() < configEntry.getValue().getMinCount()) {
                // 钉钉
                String message = PREFIX_CONSUMER_DOWN + " ，消费组：" + consumeInfo.getGroup()
                        + "，当前消费者数量：" + consumeInfo.getCount() + "，阈值：" + configEntry.getValue().getMinCount();
                DingTalkExportHandler.export(message);
            }

            if (consumeInfo.getDiffTotal() > configEntry.getValue().getMaxDiffTotal()) {
                // 钉钉
                String message = PREFIX_MESSAGE_STACKING + " ，消费组：" + consumeInfo.getGroup()
                        + "，消息堆积值：" + consumeInfo.getDiffTotal() + "，阈值：" + configEntry.getValue().getMaxDiffTotal();
                DingTalkExportHandler.export(message);
            }
        }
    }

}
