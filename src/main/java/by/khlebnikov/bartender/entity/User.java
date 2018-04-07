package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Date;

@XmlRootElement
public class User {
    private String name;
    private String email;
    private byte[] hashKey;
    private byte[] salt;
    private Date reistrationDate;
    private String uniqueCookie;

    /*no-arg constructor is used by Jesrsey, etc.*/
    public User() { }

    public User(String name, String email, byte[] hashKey, byte[] salt) {
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
        this.salt = salt;
    }

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

    public Date getReistrationDate() {
        return reistrationDate;
    }

    public void setReistrationDate(Date reistrationDate) {
        this.reistrationDate = reistrationDate;
    }

    public String getUniqueCookie() {
        return uniqueCookie;
    }

    public void setUniqueCookie(String uniqueCookie) {
        this.uniqueCookie = uniqueCookie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (!Arrays.equals(hashKey, user.hashKey)) return false;
        if (!Arrays.equals(salt, user.salt)) return false;
        return uniqueCookie != null ? uniqueCookie.equals(user.uniqueCookie) : user.uniqueCookie == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(hashKey);
        result = 31 * result + Arrays.hashCode(salt);
        result = 31 * result + (uniqueCookie != null ? uniqueCookie.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hashKey=" + Arrays.toString(hashKey) +
                ", salt=" + Arrays.toString(salt) +
                ", uniqueCookie='" + uniqueCookie + '\'' +
                '}';
    }
}
