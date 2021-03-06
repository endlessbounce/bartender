package by.khlebnikov.bartender.tag;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Class representing the MessageTag
 */
public class MessageTag extends TagSupport {

    // Vars ---------------------------------------------------------------------------------------
    private MessageType type;
    private String locale = Constant.EMPTY;

    // Setters ------------------------------------------------------------------------------------
    public void setType(MessageType type) {
        this.type = type;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Creates a custom tag to show context messages to users
     * @return
     * @throws JspException
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            String message = Constant.EMPTY;
            if (type != null) {
                message = PropertyReader.getMessageProperty(type.getMessageKey(), locale);
            }
            pageContext.getOut().write("<p>" + message + "</p>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
