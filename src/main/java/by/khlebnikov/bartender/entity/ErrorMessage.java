package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This class is used to substitute error page send by Jersey to the browser in case
 * of an error by a JSON object
 */
@XmlRootElement
public class ErrorMessage implements Serializable {

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private String message;
    private int code;
    private String documentation;

    // Constructors -------------------------------------------------------------------------------
    public ErrorMessage() {
    }

    public ErrorMessage(String message, int code, String documentation) {
        this.message = message;
        this.code = code;
        this.documentation = documentation;
    }

    // Getters and Setters ------------------------------------------------------------------------
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * Compares this ErrorMessage to another one.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorMessage that = (ErrorMessage) o;

        if (code != that.code) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return documentation != null ? documentation.equals(that.documentation) : that.documentation == null;
    }

    /**
     * Returns a hashcode for this ErrorMessage.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + code;
        result = 31 * result + (documentation != null ? documentation.hashCode() : 0);
        return result;
    }

    /**
     * Returns the String representation of this ErrorMessage.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", documentation='" + documentation + '\'' +
                '}';
    }
}
