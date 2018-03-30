package by.khlebnikov.bartender.tag;

import by.khlebnikov.bartender.manager.PropertyManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class MessageTag extends TagSupport {
    private MessageType type;

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            String message = "";
            if(type!=null){
                message = PropertyManager.getMessageProperty(type.getMessageKey());
            }
            pageContext.getOut().write("<p>" + message + "</p>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
