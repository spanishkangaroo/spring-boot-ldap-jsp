package es.devpamplona.web.ldap;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

/**
 * Custom class to retrieve an attribute named "nIF" from an LDAP entry.
 * 
 * <p>
 * The rest of the functionality is inherited from <tt>LdapUserDetailsMapper</tt>
 * <p>
 * 
 * @author javier.vazquez
 *
 */
public class CustomLdapUserDetailsContextMapper extends LdapUserDetailsMapper {
	// ~ Instance fields
	// ================================================================================================

	private final Log logger = LogFactory.getLog(CustomLdapUserDetailsContextMapper.class);
	private String passwordAttributeName = "userPassword";
	private String nifAttributeName = "nIF";
	private String[] roleAttributes = null;

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		String dn = ctx.getNameInNamespace();

		logger.debug("Mapping user details from context with DN: " + dn);

		CustomLdapUserDetailsImpl.Essence essence = new CustomLdapUserDetailsImpl.Essence();
		essence.setDn(dn);

		Object passwordValue = ctx.getObjectAttribute(passwordAttributeName);

		if (passwordValue != null) {
			essence.setPassword(mapPassword(passwordValue));
		}

		essence.setUsername(username);

		Object nifValue = ctx.getObjectAttribute(nifAttributeName);

		if (nifValue != null) {
			essence.setNif(String.valueOf(nifValue));
		}

		// Map the roles
		for (int i = 0; (roleAttributes != null) && (i < roleAttributes.length); i++) {
			String[] rolesForAttribute = ctx.getStringAttributes(roleAttributes[i]);

			if (rolesForAttribute == null) {
				logger.debug("Couldn't read role attribute '" + roleAttributes[i] + "' for user " + dn);
				continue;
			}

			for (String role : rolesForAttribute) {
				GrantedAuthority authority = createAuthority(role);

				if (authority != null) {
					essence.addAuthority(authority);
				}
			}
		}

		// Add the supplied authorities

		for (GrantedAuthority authority : authorities) {
			essence.addAuthority(authority);
		}

		// Check for PPolicy data

		PasswordPolicyResponseControl ppolicy = (PasswordPolicyResponseControl) ctx
				.getObjectAttribute(PasswordPolicyControl.OID);

		if (ppolicy != null) {
			essence.setTimeBeforeExpiration(ppolicy.getTimeBeforeExpiration());
			essence.setGraceLoginsRemaining(ppolicy.getGraceLoginsRemaining());
		}

		return essence.createUserDetails();
	}
}
