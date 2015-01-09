package cn.edu.ustc.wade.db.iBatis;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.edu.ustc.wade.db.iBatis.dao.UserDao;
import cn.edu.ustc.wade.db.iBatis.dto.UserDto;

public class UserTest {
    private static final Log logger = LogFactory.getLog(UserTest.class);
    private static SqlSessionFactoryBuilder sqlSessionFactoryBuilder;
    private static SqlSessionFactory sqlSessionFactory;

    public static void main(String args[]) {
        try {
            init();
        } catch (IOException e) {
            logger.error("", e);
        }
        testQueryList();
    }

    private static void init() throws IOException {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
    }

    private static void testQueryList() {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            UserDao userDao = session.getMapper(UserDao.class);
            UserDto user = new UserDto();
            user.setUsername("iMbatis");
            List<UserDto> users = userDao.queryUsers(user);
            session.commit(true);
            if (null != users) {
                // Mybatis的日志级别竟然没得info，只好用error代替了
                logger.error("Find " + users.size() + " users named iMbatis.");
                System.out.println(users.get(0).getAddress());
            }
            
        } catch (Exception e) {
            logger.error("", e);
            session.rollback(true);
        } finally {
            session.close();
        }
    }
}
