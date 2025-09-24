package org.jboss.as.quickstarts.kitchensink;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderIndexPage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("members"))
            .andExpect(content().string(containsString("Member Registration")));
    }

    @Test
    void shouldValidateFormSubmission() throws Exception {
        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("email", "not-an-email")
                .param("phoneNumber", "123"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeHasFieldErrors("newMember", "name", "email", "phoneNumber"));
    }

    @Test
    void shouldSubmitFormSuccessfully() throws Exception {
        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Alice")
                .param("email", "alice@mailinator.com")
                .param("phoneNumber", "2125551215"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("successMessage"))
            .andExpect(content().string(containsString("Registration successful")));

        mockMvc.perform(get("/rest/members"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }
}
