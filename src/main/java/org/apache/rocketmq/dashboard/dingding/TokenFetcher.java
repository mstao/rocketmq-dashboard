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

/**
 * 获取钉钉token
 */
public class TokenFetcher {
    private static final String DING_TALK_TOKEN = "dingding.token";

    /**
     * 解析token，顺序：
     *
     * <ul>
     *   <li>1. 如果指定token不为空，直接返回</li>
     *   <li>2. 通过SpringBoot Property获取，如果存在，返回</li>
     *   <li>3. 通过Java 系统参数获取，如果存在，返回</li>
     *   <li>4. 如果不存在，返回空</li>
     * </ul>
     *
     * @param token 指定token
     * @return token
     */
    public static String resolveToken(String token) {
        if (token != null) {
            return token;
        }

        return System.getProperty(DING_TALK_TOKEN);
    }
}
