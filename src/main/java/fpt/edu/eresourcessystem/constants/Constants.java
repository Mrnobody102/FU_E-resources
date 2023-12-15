package fpt.edu.eresourcessystem.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {
    public static final Integer PAGE_SIZE = 10;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public static final String VERIFICATION_CODE = "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";

    public static final String SAMPLE_ADMIN = "admin";
    public static final String SAMPLE_LIBRARIAN = "user";
    public static final String SAMPLE_LECTURER = "lecturer";
    public static final String SAMPLE_STUDENT = "student";

    public static final long DATABASE_MAX_SIZE_FILE = 1048576;
    public static final long MAX_SIZE_FILE = 104857600;
    public static final long MAX_SIZE_SUPPORTING_FILE = 52428800;


}
