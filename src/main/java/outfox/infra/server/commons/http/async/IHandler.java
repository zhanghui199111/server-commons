package outfox.infra.server.commons.http.async;

import org.apache.http.HttpResponse;

/**
 * author: zhn4528
 * create: 2020/12/2 19:35
*/
public interface IHandler {

    /**
     * 处理异常时，执行该方法
     * @return
     */
    Object failed(Exception e);

    /**
     * 处理正常时，执行该方法
     * @return
     */
    Object completed(HttpResponse httpResponse);

    /**
     * 处理取消时，执行该方法
     * @return
     */
    Object cancelled();

}
