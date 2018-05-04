package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class represents the Prospect User model. Prospect user is a perspective user, who is trying
 * to register into the system, but has not confirmed his registration via email yet.
 */
@XmlRootElement
public class ProspectUser implements Serializable {

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private String name;
    private String email;
    private byte[] hashKey;
    private byte[] salt;
    private long expiration;
    private String code;

    // Constructors -------------------------------------------------------------------------------
    public ProspectUser() {
    }

    public ProspectUser(String name, String email, byte[] hashKey, byte[] salt, long expiration, String code) {
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
        this.salt = salt;
        this.expiration = expiration;
        this.code = code;
    }

    // Getters and Setters ------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getHashKey() {
        return hashKey;
    }

    public void setHashKey(byte[] hashKey) {
        this.hashKey = hashKey;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * The prospect user email is unique for each Prospect User.
     * So this should compare Prospect User by email only.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProspectUser that = (ProspectUser) o;

        return email != null ? email.equals(that.email) : that.email == null;
    }

    /**
     * The prospect user email is unique for each Prospect User.
     * So Prospect User with same email should return same hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    /**
     * Returns the String representation of this Prospect User.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProspectUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hashKey=" + Arrays.toString(hashKey) +
                ", salt=" + Arrays.toString(salt) +
                ", expiration=" + expiration +
                ", code=" + code +
                '}';
    }
}
