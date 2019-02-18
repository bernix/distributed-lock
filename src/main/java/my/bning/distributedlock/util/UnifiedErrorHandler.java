package my.bning.distributedlock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@ControllerAdvice
public class UnifiedErrorHandler {

    private static Map<String, String > res = new HashMap<>(16);

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Object processException(HttpServletRequest req, Exception e) {
        res.put("url", req.getRequestURL().toString());
        if (e instanceof RuntimeException) {
            res.put("message", e.getMessage());
        } else {
            res.put("message", "sorry error happens");
        }
        return res;
    }
}
