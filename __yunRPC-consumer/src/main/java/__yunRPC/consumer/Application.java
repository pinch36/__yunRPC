package __yunRPC.consumer;

import __yunRPC.common.model.User;
import __yunRPC.common.service.UserService;
import __yunRPC.core.factory.ServiceFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/16:38
 * @Description:
 */
public class Application {
    public static void main(String[] args) {
        UserService userService = ServiceFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("__yun");
        user = userService.getUser(user);
        user = userService.getUser(user);
        user = userService.getUser(user);
        if (user != null){
            System.out.println("用户名："+user.getName());
        }else{
            System.out.println("用户名null");
        }
    }
}
