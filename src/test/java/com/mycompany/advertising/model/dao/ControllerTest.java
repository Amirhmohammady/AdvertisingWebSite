package com.mycompany.advertising.model.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Amir on 8/4/2022.
 */
//@WebMvcTest(controllers = TestCotroller.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:D:/Developer/apache-tomcat-9.0.24--windows-x64/amirExtraFiles/adv-web/applicationte-test.properties")
public class ControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilterChainProxy springSecurityFilter;

    //    @MockBean
    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testException() {
        Assert.assertNotNull(context);
        Assert.assertNotNull(springSecurityFilter);
        //Mockito.when()
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilter)
                .build();
        try {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/test124/5")//("/test123")//("/forums/{forumId}/register", 42L)
                    //.contentType(MediaType.TEXT_HTML);//(MediaType.APPLICATION_JSON)//("application/json")
                    //.param("sendWelcomeMail", "true")
                    //.content("");//objectMapper.writeValueAsString(user)
            );
            resultActions.andDo(MockMvcResultHandlers.print());
            resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/test124/5"));
            resultActions.andDo(MockMvcResultHandlers.print());
            resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/test124/5"));
            resultActions.andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
