package xa.sh.bank.bank.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import xa.sh.bank.bank.security.services.UserDetailsImpl;

@Component
public final class SecurityUtils {

    public SecurityUtils() {}

    /**
     * Retrieve the current authenticated UserDetailsImpl.
     * @throws AccessDeniedException if no authenticated user is found.
     */
    public static UserDetails getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User is not authenticated.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        throw new IllegalStateException(
            "Unexpected principal type found in SecurityContext: " 
            + principal.getClass().getName()
        );
    }

    /**
     * Return the ID of the authenticated user.
     * Gives id of customer , so i can use findById method of customerRepo
     */
    public static String getAuthenticatedUserId() {
        UserDetails user = getAuthenticatedUserDetails();
        // assuming your UserDetailsImpl#getId() returns String
        return ((UserDetailsImpl) user).getId();
    }

    /**
     * Ensure the authenticated user’s ID matches the given ID.
     * @throws AccessDeniedException if they don’t match.
     */
    public static void checkCurrentUserMatchesId(String idToCheck) {
        String currentUserId = getAuthenticatedUserId();
        if (!currentUserId.equals(idToCheck)) {
            throw new AccessDeniedException(
                "Authenticated user ID (" + currentUserId
                + ") does not match the required ID (" + idToCheck + ")"
            );
        }
    }

    /**
     * Check if the current user has the given role (e.g. "ROLE_ADMIN").
     */
    public static boolean hasRole(String roleName) {
        UserDetails user = getAuthenticatedUserDetails();
        return user.getAuthorities().stream()
                   .anyMatch(a -> a.getAuthority().equals(roleName));
    }
}
