package com.wen.tools.scp;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.wen.tools.domain.config.IConstantsDomain;
import com.wen.tools.log.utils.LogUtil;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class SCPUtils {

    private static Logger logger = LogUtil.getCoreLog(SCPUtils.class);

    private Connection connection;


    private String ip;
    private int port = 22;
    private String userName = IConstantsDomain.SCP.DEFAULT_USER_NAME;
    private String password;
    private String privateKey = IConstantsDomain.SCP.DEFAULT_PRIVATE_KEY;
    private boolean usePassword=false;


    public SCPUtils(String ip, int port) {
        this.ip = ip;
        this.port = port;
        usePassword = false;
        connection = new Connection(ip, port);
    }

    public SCPUtils(String ip, int port, String userName, String passward) {
        connection = new Connection(ip, port);
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = passward;
    }

    public SCPUtils(String userName, String privateKey, String ip, int port) {
        connection = new Connection(ip, port);
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.usePassword = false;
        this.privateKey=privateKey;
        connection = new Connection(ip, port);
    }


    public SCPUtils(String ip, int port, String userName) {
        connection = new Connection(ip, port);
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.usePassword = false;
        connection = new Connection(ip, port);
    }

    public boolean isAuthedWithPassword(String user, String password) throws IOException {
        return connection.authenticateWithPassword(user, password);
    }

    public boolean isAuthedWithPublicKey(String user, File privateKey) {
        try {
            return connection.authenticateWithPublicKey(user, privateKey,null);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    public boolean isAuth() throws IOException {
        if (usePassword) {
            return isAuthedWithPassword(userName, password);
        } else {
            return isAuthedWithPublicKey(userName, new File(privateKey));
        }
    }

    public void getFile(String remoteFile, String path) {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                logger.info("auth ok");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.get(remoteFile,path);
            } else {
                throw new RuntimeException("auth failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            SCPUtils scpUtils=new SCPUtils("127.0.0.1",30019);
            scpUtils.getFile("/home/wenqchen/uc4-fjob-extract-1.0-SNAPSHOT.jar", "C:\\Users\\wenqchen\\logs\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
