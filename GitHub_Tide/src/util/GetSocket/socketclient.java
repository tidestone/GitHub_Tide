/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author yangzhen
 */
public class socketclient {

    private Socket remote_socket = null;

    private PrintWriter out = null;
    private BufferedReader in = null;
    private String remote_ip = "132.228.5.56";
    private String remote_port = "9005";
    private String CONFIG_PATH = "conf/ftp.config";

    public boolean load() {
        boolean _bs = false;
        Properties prop = new Properties();
        File file = new File(CONFIG_PATH);
        try {
            prop.load(new FileInputStream(file));
            System.getProperties().putAll(prop);
            remote_ip = prop.getProperty("sockethost");
            remote_port = prop.getProperty("sockethostport");
            System.out.println("GET Config: IP:" + remote_ip + " Port:" + remote_port);
            _bs = true;
        } catch (Exception e) {
            System.out.println("配置文件读取异常:" + e.getMessage().toString());
        }
        return _bs;
    }

    public boolean open() {
        boolean _bs = false;
        try {
            if (remote_socket == null || remote_socket.isClosed()) {
                //新建连接
                remote_socket = new Socket(remote_ip, Integer.parseInt(remote_port));
                System.out.println("Connection access:" + remote_ip + ":" + remote_port + ".");
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(remote_ip, Integer.parseInt(remote_port));
                    System.out.println("Connection access:" + remote_ip + ":" + remote_port + ".");
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            } else {
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(remote_ip, Integer.parseInt(remote_port));
                    System.out.println("Connection access:" + remote_ip + ":" + remote_port + ".");
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return _bs;
    }

    public boolean ini() {
        boolean _bs = false;
        if (load()) {
            if (open()) {
                _bs = true;
            } else {
                System.out.println("Socket 连接失败....");
            }
        } else {
            System.out.println("配置文件读取错误....");
        }
        return _bs;
    }

    public boolean sendmessage(String mes, org.apache.log4j.Logger log) {
        boolean _bs = false;
        try {
            if (mes.length() > 0 && ini()) {
                socketclient _socketclient = new socketclient();
                // socketclient.out = new PrintWriter(socketclient.remote_socket.getOutputStream(),"UTF-8");
                _socketclient.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(remote_socket.getOutputStream(), "GBK")), true);
                String mes1 = mes;
                _socketclient.out.println(mes1);
                _socketclient.out.flush();
                System.out.println("Send access:" + mes1);
                _bs = true;
                //remote_socket.close();
                //记录告警
                log.info(mes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (remote_socket != null) {
                try {
                    remote_socket.close();
                } catch (Exception e) {
                }
            }
        } finally {

        }
        return _bs;
    }
}
