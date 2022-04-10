package com.lihuia.mysterious.common.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lihuia.com
 * @date 2022/4/10 12:59 PM
 */

@Slf4j
public class SSHUtils {

    private String host;
    private Integer port;
    private String username;
    private String password;

    public SSHUtils(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public Boolean telnet(Integer timeout) {
        Socket socket = new Socket();
        Boolean isConnected = false;
        try {
            log.info("开始telnet, host:{}, port:{}", host, port);
            socket.connect(new InetSocketAddress(host, port), timeout); // 建立连接
            isConnected = socket.isConnected(); // 通过现有方法查看连通状态
            log.info("连通性状态:{}", isConnected);
            //System.out.println(isConnected);    // true为连通
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.CANNOT_CONNECT);        // 当连不通时，直接抛异常，异常捕获即可
        } finally {
            try {
                socket.close();   // 关闭连接
            } catch (IOException e) {
                throw new MysteriousException(ResponseCodeEnum.CLOSE_CONNECT_ERROR.getMessage(), e);
            }
        }
        return isConnected;
    }

    public String execCommand(String command) {
        StringBuilder returnLine = new StringBuilder();

        Connection connection = new Connection(host, port);
        Session session = null;
        try {
            log.info("建立连接, host: {}, port: {}", host, port);
            connection.connect();
            log.info("SSH校验, username: {}, password: {}", username, password);
            Boolean isAuthenticated = connection.authenticateWithPassword(username, password);
            if (Boolean.FALSE.equals(isAuthenticated)) {
                throw new IOException("Authentication failed, 执行command异常");
            }
            /** 打开一个session执行linux命令 */
            session = connection.openSession();
            log.info("exec: {}", command);
            session.execCommand(command);

            /** 执行命令控制台结果 */
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            returnLine.append(br.readLine());
            br.close();
        } catch (Exception e) {
            throw new MysteriousException(ResponseCodeEnum.SSH_EXEC_ERROR.getMessage(), e);
        } finally {
            if (null != session) {
                session.close();
            }
            connection.close();
        }
        log.info("执行命令: {}, 返回结果为: {}", command, returnLine);
        return returnLine.toString();
    }

    public void scpFile(String filePath, String remotePath) {
        /** 先创建remotePath，以免不存在报错 */
        execCommand("mkdir -p " + remotePath);
        /** 文件scp到数据服务器 */
        Connection connection = new Connection(host, port);
        try {
            connection.connect();
            Boolean isAuthenticated = connection.authenticateWithPassword(username, password);
            if (Boolean.FALSE.equals(isAuthenticated)) {
                throw new IOException("Authentication failed, 文件scp异常");
            }
            SCPClient scp = new SCPClient(connection);
            log.info("scp文件开始: {}", filePath);
            scp.put(filePath, remotePath);
        } catch (IOException e) {
            log.info("文件scp发生异常", e);
        } finally {
            connection.close();
        }
        log.info("scp文件结束: {}", filePath);
    }

}
