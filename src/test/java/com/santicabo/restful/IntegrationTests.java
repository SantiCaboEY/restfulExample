package com.santicabo.restful;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void createUserOk() {
        String jsonRequest = """
                 {
                        "name": "Juan Rodriguez",
                        "email": "juan@rodriguez.org",
                        "password": "hUnter22",
                        "phones": [
                            {
                                "number": "1234567",
                                "citycode": "1",
                                "contrycode": "57"
                            }
                        ]
                    }
                """;

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        //Mismo usuario. Devuelve que ya existe
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void createUserValidationError(){
        String jsonRequest = """
                 {
                        "name": "Juan Rodriguez",
                        "email": "cualquiercosa",
                        "password": "34234876&%$#$Ya",
                        "phones": [
                            {
                                "number": "1234567",
                                "citycode": "1",
                                "contrycode": "57"
                            }
                        ]
                    }
                """;

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


}
