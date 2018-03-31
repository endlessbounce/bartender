package by.khlebnikov.bartender.tag;

import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class MessageTag extends TagSupport {
    private MessageType type;
    private String locale = "";

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            String message = "";
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
