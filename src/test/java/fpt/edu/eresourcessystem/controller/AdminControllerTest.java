//package fpt.edu.eresourcessystem.controller;
//
//import fpt.edu.eresourcessystem.config.security.CustomizeAuthenticationSuccessHandler;
//import fpt.edu.eresourcessystem.config.security.SecurityConfig;
//import fpt.edu.eresourcessystem.repository.AccountRepository;
//import fpt.edu.eresourcessystem.service.AccountService;
//import fpt.edu.eresourcessystem.service.security.CustomizeUserDetailsService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//// Other necessary imports
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(AdminController.class)
//@Import({SecurityConfig.class, CustomizeUserDetailsService.class})
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AccountService accountService; // Mock all the services
//
//    @MockBean
//    private AccountRepository accountRepository;
//
//    @MockBean
//    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
//
//    @Test
//    public void testGetLibrarianDashboard() throws Exception {
//        mockMvc.perform(get("/admin"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("admin/admin_dashboard"));
//    }
//
//    // Add more tests for other methods
//}
