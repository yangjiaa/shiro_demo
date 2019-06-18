import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {
    //创建数据源
    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }

    @Test
    public void testAuthentication() {

        JdbcRealm jdbcRealm = new JdbcRealm();

        jdbcRealm.setDataSource(dataSource);
        //设置权限开关为true(默认为false)
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql="select password from test_user where user_name = ? ";
        jdbcRealm.setAuthenticationQuery(sql);
        String roleSql="select role_name from test_user_role where user_name=?" ;

        jdbcRealm.setUserRolesQuery(roleSql);
        //构建defaultSecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //2.主体提交认证请求

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "123456");
        subject.login(token);
        System.out.println("isAuthenticated：" + subject.isAuthenticated());
        /*subject.logout();
        System.out.println("isAuthenticated："+subject.isAuthenticated());*/
       /* subject.checkRole("admin");
        subject.checkRoles("admin","user");
        subject.checkPermission("user:delete");*/
       subject.checkRole("user");
    }
}
