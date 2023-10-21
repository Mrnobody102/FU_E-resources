package fpt.edu.eresourcessystem.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RedirectUtil {

    public static String getRedirectUrl(Authentication authentication) {
        // Stores granted authorities of the authenticated user into auth.
        // Note: This is not a MongoDB collection, but the Java collection class.

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();

        // Loops through each of the authorities and redirect the user to their
        // respective home pages.

        for (GrantedAuthority grantedAuthority : auth) {

            if (grantedAuthority.getAuthority().equals("ROLE_LIBRARIAN")) {

                return "/librarian";

            } else if (grantedAuthority.getAuthority().equals("ROLE_LECTURER")) {

                return "/lecturer";

            } else if (grantedAuthority.getAuthority().equals("ROLE_STUDENT")) {

                return "/student";

            }
        }
        return "/login?error";
    }

}