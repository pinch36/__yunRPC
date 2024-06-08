package __yunRPC.provider;

import __yunRPC.common.model.User;
import __yunRPC.common.service.UserService;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/16:37
 * @Description:
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
