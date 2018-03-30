package by.khlebnikov.bartender.entity;

public class ProspectUser {
    private String name;
    private String email;
    private String password;
    private long expiration;
    private long code;

    public ProspectUser(String name, String email, String password, long expiration, long code) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (int) (expiration ^ (expiration >>> 32));
        result = 31 * result + (int) (code ^ (code >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ProspectUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", expiration=" + expiration +
                ", code='" + code + '\'' +
                '}';
    }
}
