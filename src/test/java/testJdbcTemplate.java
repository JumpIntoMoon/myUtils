import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-05-09 17:11
 **/

@RunWith(SpringJUnit4ClassRunner.class)//使项目从spring启动
@ContextConfiguration("classpath:applicationContext.xml")//读取spring配置文件
@WebAppConfiguration("")//静态资源根路径，不加该注解时，如果spring配置文件中有静态资源路径，有可能造成启动异常
public class testJdbcTemplate {

    private ApplicationContext ctx;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void getApplicationCtx() {
        //当@RunWith注解和@ContextConfiguration注解失效时可以使用这种方法进行spring测试
        //this.ctx = new ClassPathXmlApplicationContext("applicationContext.xml","spring-mvc.xml");
        //this.jdbcTemplate = (JdbcTemplate)this.ctx.getBean("jdbcTemplate");
    }

    @Test
    public void testJdbcTemplate() {
        Map map = jdbcTemplate.queryForMap("select * from person limit 1");
        System.out.println(map.toString());
    }

}
