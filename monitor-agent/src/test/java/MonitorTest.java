import com.envisioniot.uscada.monitor.agent.MonitorAgentApplication;
import com.envisioniot.uscada.monitor.agent.service.monitor.impl.AppServiceImpl;
import com.envisioniot.uscada.monitor.agent.service.monitor.impl.HostServiceImpl;
import com.envisioniot.uscada.monitor.common.entity.AppObj;
import com.envisioniot.uscada.monitor.common.entity.AppStat;
import com.envisioniot.uscada.monitor.common.entity.CommStat;
import com.envisioniot.uscada.monitor.common.entity.HostStat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.luo
 * @date 2020-12-31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonitorAgentApplication.class)
public class MonitorTest {

    @Autowired
    private AppServiceImpl appService;


    @Test
    public void getApp(){
    }

    @Autowired
    private HostServiceImpl hostService;

    @Test
    public void testHost(){
        HostStat sample = hostService.getMonitorSample(null);
        System.out.println(sample);
    }
}
