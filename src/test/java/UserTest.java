import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.junit.Test;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  20:06
 */
public class UserTest {
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Test
    public void getUserByName(){
        User user = new User();
        user.setLoginAct("zs");
        user.setLoginPwd("202cb962ac59075b964b07152d234b70");
        System.out.println(userDao.getUserByNameAndPw(user));

       // MD5Util.getMD5("202cb962ac59075b964b07152d234b70");
    }
}
