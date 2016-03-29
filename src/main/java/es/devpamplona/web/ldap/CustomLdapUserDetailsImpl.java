package es.devpamplona.web.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.ppolicy.PasswordPolicyData;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.util.Assert;

/**
 * A UserDetails implementation which is used internally by the Ldap services. It also contains the user's
 * distinguished name and a set of attributes that have been retrieved from the Ldap server, including the 
 * custom attribute "nIF". This class is basically <tt>LdapUserDetailsImpl</tt> with one more field.  
 * <p>
 * An instance may be created as the result of a search, or when user information is retrieved during authentication.
 * <p>
 * An instance of this class will be used by the <tt>LdapAuthenticationProvider</tt> to construct the final user details
 * object that it returns.
 * <p>
 * The {@code equals} and {@code hashcode} methods are implemented using the {@code Dn} property and do not consider
 * additional state, so it is not possible two store two instances with the same DN in the same set, or use them as
 * keys in a map.
 * 
 * @author javier.vazquez
 */
public class CustomLdapUserDetailsImpl implements LdapUserDetails, PasswordPolicyData {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	//~ Instance fields ================================================================================================

    private String dn;
    private String password;
    private String username;
    private Collection<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private String nif;
    // PPolicy data
    private int timeBeforeExpiration = Integer.MAX_VALUE;
    private int graceLoginsRemaining = Integer.MAX_VALUE;

    //~ Constructors ===================================================================================================

    protected CustomLdapUserDetailsImpl() {}

    //~ Methods ========================================================================================================

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getDn() {
        return dn;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public String getNif() {
        return nif;
    }

    public int getTimeBeforeExpiration() {
        return timeBeforeExpiration;
    }

    public int getGraceLoginsRemaining() {
        return graceLoginsRemaining;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomLdapUserDetailsImpl) {
            return dn.equals(((CustomLdapUserDetailsImpl)obj).dn);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return dn.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Dn: ").append(dn).append("; ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("NIF: ").append(this.nif).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("CredentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (this.getAuthorities() != null && !this.getAuthorities().isEmpty()) {
            sb.append("Granted Authorities: ");
            boolean first = true;

            for (Object authority : this.getAuthorities()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }

                sb.append(authority.toString());
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }

    //~ Inner Classes ==================================================================================================

    /**
     * Variation of essence pattern. Used to create mutable intermediate object
     */
    public static class Essence {
        protected CustomLdapUserDetailsImpl instance = createTarget();
        private List<GrantedAuthority> mutableAuthorities = new ArrayList<GrantedAuthority>();

        public Essence() { }

        public Essence(DirContextOperations ctx) {
            setDn(ctx.getDn());
        }

        public Essence(LdapUserDetails copyMe) {
            setDn(copyMe.getDn());
            setUsername(copyMe.getUsername());
            setPassword(copyMe.getPassword());
            setEnabled(copyMe.isEnabled());
            setAccountNonExpired(copyMe.isAccountNonExpired());
            setCredentialsNonExpired(copyMe.isCredentialsNonExpired());
            setAccountNonLocked(copyMe.isAccountNonLocked());
            setAuthorities(copyMe.getAuthorities());
        }

        protected CustomLdapUserDetailsImpl createTarget() {
            return new CustomLdapUserDetailsImpl();
        }

        /** Adds the authority to the list, unless it is already there, in which case it is ignored */
        public void addAuthority(GrantedAuthority a) {
            if(!hasAuthority(a)) {
                mutableAuthorities.add(a);
            }
        }

        private boolean hasAuthority(GrantedAuthority a) {
            for (GrantedAuthority authority : mutableAuthorities) {
                if(authority.equals(a)) {
                    return true;
                }
            }
            return false;
        }

        public LdapUserDetails createUserDetails() {
            Assert.notNull(instance, "Essence can only be used to create a single instance");
            Assert.notNull(instance.username, "username must not be null");
            Assert.notNull(instance.getDn(), "Distinguished name must not be null");

            instance.authorities = Collections.unmodifiableList(mutableAuthorities);

            LdapUserDetails newInstance = instance;

            instance = null;

            return newInstance;
        }

        public Collection<GrantedAuthority> getGrantedAuthorities() {
            return mutableAuthorities;
        }

        public void setAccountNonExpired(boolean accountNonExpired) {
            instance.accountNonExpired = accountNonExpired;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
            instance.accountNonLocked = accountNonLocked;
        }

        public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
            mutableAuthorities = new ArrayList<GrantedAuthority>();
            mutableAuthorities.addAll(authorities);
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
            instance.credentialsNonExpired = credentialsNonExpired;
        }

        public void setDn(String dn) {
            instance.dn = dn;
        }

        public void setDn(Name dn) {
            instance.dn = dn.toString();
        }

        public void setEnabled(boolean enabled) {
            instance.enabled = enabled;
        }
        
        public void setNif(String nif){
        	instance.nif = nif;
        }

        public void setPassword(String password) {
            instance.password = password;
        }

        public void setUsername(String username) {
            instance.username = username;
        }

        public void setTimeBeforeExpiration(int timeBeforeExpiration) {
            instance.timeBeforeExpiration = timeBeforeExpiration;
        }

        public void setGraceLoginsRemaining(int graceLoginsRemaining) {
            instance.graceLoginsRemaining = graceLoginsRemaining;
        }   
    }
}
