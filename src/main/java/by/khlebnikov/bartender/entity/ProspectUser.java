package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement
public class ProspectUser {
    private String name;
    private String email;
    private byte [] hashKey;
    private byte [] salt;
    private long expiration;
    private long code;

    /*no-arg constructor is used by Jesrsey, etc.*/
    public ProspectUser() { }

    public ProspectUser(String name, String email, byte[] hashKey, byte[] salt, long expiration, long code) {
        this.name = name;
        this.email = email;
        this.hashKey = hashKey;
        this.salt = salt;
        this.expiration = expiration;
        this.code = code;
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

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProspectUser that = (ProspectUser) o;

        if (expiration != that.expiration) return false;
        if (code != that.code) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (!Arrays.equals(hashKey, that.hashKey)) return false;
        return Arrays.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(hashKey);
        result = 31 * result + Arrays.hashCode(salt);
        result = 31 * result + (int) (expiration ^ (expiration >>> 32));
        result = 31 * result + (int) (code ^ (code >>> 32));
        return result;
    }

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
