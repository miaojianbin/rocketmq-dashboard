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
package org.apache.rocketmq.dashboard.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.rocketmq.dashboard.service.strategy.UserContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserContext userContext;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(loginService, "userContext", userContext);
    }

    @Test
    public void shouldRedirectToContextPathLoginPageWhenAuthRequired() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/rocketmq-console");
        request.setRequestURI("/rocketmq-console/topic/list.query");
        request.setRequestURL(new StringBuffer("http://localhost:8080/rocketmq-console/topic/list.query"));
        request.setQueryString("page=1");

        MockHttpServletResponse response = new MockHttpServletResponse();

        loginService.auth(request, response);

        String encodedUrl = URLEncoder.encode(
            "http://localhost:8080/rocketmq-console/topic/list.query?page=1",
            StandardCharsets.UTF_8
        );
        assertEquals("/rocketmq-console/#/login?redirect=" + encodedUrl, response.getRedirectedUrl());
        assertEquals(HttpServletResponse.SC_FOUND, response.getStatus());
    }
}
