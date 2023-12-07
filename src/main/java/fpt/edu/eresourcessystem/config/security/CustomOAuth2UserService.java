package fpt.edu.eresourcessystem.config.security;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.CustomOAuth2User;
import fpt.edu.eresourcessystem.model.Student;
import fpt.edu.eresourcessystem.repository.AccountRepository;
import fpt.edu.eresourcessystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fpt.edu.eresourcessystem.constants.Constants.VERIFICATION_CODE;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;
    private final StudentService studentService;

    @Autowired
    public CustomOAuth2UserService(AccountRepository accountRepository, StudentService studentService) {
        this.accountRepository = accountRepository;
        this.studentService = studentService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User user =  super.loadUser(userRequest);
        String email = user.<String>getAttribute("email");
        Account account = accountRepository.findByEmail(email).orElse(null);
        if(account == null) {
            Account newAccount = new Account();
            newAccount.setEmail(email);
            newAccount.setName(user.<String>getAttribute("name"));
            newAccount.setRole(AccountEnum.Role.STUDENT);
            newAccount.setStatus(AccountEnum.Status.ACTIVE);
            newAccount.setPassword(VERIFICATION_CODE);
            newAccount.setDeleteFlg(CommonEnum.DeleteFlg.PRESERVED);
            newAccount.setAccountType(AccountEnum.AccountType.FPT_MAIL_ACC);
            account = accountRepository.insert(newAccount);
            Student student = new Student();
            student.setAccount(account);
            studentService.addStudent(student);
        }
        // Granted Authority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().toString());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        OAuth2User oAuth2User = new CustomOAuth2User(user, clientName, authorities);
        return oAuth2User;
    }

}