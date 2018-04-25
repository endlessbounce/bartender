package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * This class represent the User model.
 */
@XmlRootElement
public class User implements Serializable{

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private int id;
    private String name;
    private String email;
    private byte[] hashKey;
    private byte[] salt;
    private Date registrationDate;
    private String uniqueCookie;

    // Constructors -------------------------------------------------------------------------------
    public User() { }

    public User(String name, String email, byte[] hashKey, byte[] salt) {
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
        this.salt = salt;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUniqueCookie() {
        return uniqueCookie;
    }

    public void setUniqueCookie(String uniqueCookie) {
        this.uniqueCookie = uniqueCookie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * The user ID is unique for each User. So this should compare User by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    /**
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns the String representation of this User.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hashKey=" + Arrays.toString(hashKey) +
                ", salt=" + Arrays.toString(salt) +
                ", registrationDate=" + registrationDate +
                ", uniqueCookie='" + uniqueCookie + '\'' +
                '}';
    }
}
