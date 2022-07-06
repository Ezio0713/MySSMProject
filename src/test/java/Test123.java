import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.spring.SqlSessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Test123 {
    @Test
    public void test2() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ActivityMapper mapper = sqlSession.getMapper(ActivityMapper.class);
        for (int i=0;i<99;i++){
            String uuid = UUIDUtils.getUUID();
            Activity activity = new Activity();
            activity.setId(uuid);
            activity.setEditBy("06f5fc056eac41558a964f96daa7f27c");
            activity.setCreateBy("06f5fc056eac41558a964f96daa7f27c");
            activity.setOwner("06f5fc056eac41558a964f96daa7f27c");
            activity.setName("我的"+i);
            mapper.insert(activity);
            //测试提交
        }
    }
}