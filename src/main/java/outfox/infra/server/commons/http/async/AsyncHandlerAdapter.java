package outfox.infra.server.commons.http.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;

/**
 * author: zhn4528
 * create: 2020/12/2 19:20
*/
@Slf4j
public class AsyncHandlerAdapter implements IHandler {

    @Override
    public Object failed(Exception e) {
        log.error(Thread.currentThread().getName() + " failure::" + e.getClass().getName() + "::" + ExceptionUtils.getStackTrace(e));
        return null;
    }
    @Override
    public Object completed(HttpResponse httpResponse) {
        log.debug(Thread.currentThread().getName() + " success::" + httpResponse.toString());
        return null;
    }
    @Override
    public Object cancelled() {
        log.warn(Thread.currentThread().getName() + " cancel");
        return null;
    }

}
