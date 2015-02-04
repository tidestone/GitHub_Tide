package z.xn_ping;

import java.io.File;
import java.util.List;



/**
 *
 * @author yangzhen
 * @author 功能：短信发送
 * @author 描述：
 * @author 系统内部短信发送，用于信息提醒。  电信走动环，移动联通走综调。  
 * @author 动环的需要等一段时间才有回执，综调可以立刻得到回执。   通过回执的状态判断短信的发送状态
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnping.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms(); 
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);

  

    public void run() {
        //加载数据库
        if (db.ini(log,_csnms)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 60 * 1);//60秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doing_main() {
       File file1 = new File("pingper/");//-------------------------------------------------------------
       File file2 = new File("pingper/dcn/");//                                 获
       File file3 = new File("pingper/wx/");//                                  
       File file4 = new File("pingper/lyg/");//                                 取
       File file5 = new File("pingper/jyw/");//                                 
       File file6 = new File("pingper/sz/");//                                 
       fun f=new fun();                     //                                  件
       List file=f.huoqwuwenjian(file1);//                                 
       List file22=f.huoqwuwenjian(file2);//                                    文
       List file33=f.huoqwuwenjian(file3);//                                 
       List file44=f.huoqwuwenjian(file4);//                                    
       List file55=f.huoqwuwenjian(file5);//                                 
       List file66=f.huoqwuwenjian(file6);//                                 
       file.add(file22);//                                 
       file.add(file33);//                                 
       file.add(file44);//                                 
       file.add(file55);//                                 
       file.add(file66);//--------------------------------------------------------------------------------
       //传入文件名，处理文件
       f.chulwj(log, file, _csnms);
    }

}
