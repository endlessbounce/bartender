package by.khlebnikov.bartender.exception;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps all exceptions on the server side to Response by JAX-RS.
 * This class is necessary to replace standard Tomcat error page
 * with a JSON object
 */
@Provider
public class ResourceExceptionMapper implements ExceptionMapper<Throwable>{

    /**
     * Catches all internal exceptions
     * @param ex
     * @return
     */
    @Override
    public Response toResponse(Throwable ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Constant.ERROR_500, Constant.URL_BARTENDER);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}
