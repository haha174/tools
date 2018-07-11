package com.wen.tools.sftp;

import com.jcraft.jsch.*;
import nyla.solutions.global.util.Cryption;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SftpUtil {

    public static void main(String[] args) throws Exception {
//        ChannelSftp sftp = getSftpConnect("192.168.121.6", 22, "root", "root");
//        exit(sftp);
//        System.exit(0);
    }

    /**
     * get sftp connect
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws JSchException
     */
    public static ChannelSftp getSftpConnect(final String host, final int port, final String username, final String password) throws JSchException {
        ChannelSftp sftp = null;
        JSch jsch = new JSch ();
        jsch.getSession ( username, host, port );
        Session sshSession = jsch.getSession ( username, host, port );
        sshSession.setPassword ( Cryption.interpret ( password ) );
        Properties sshConfig = new Properties ();
        sshConfig.put ( "StrictHostKeyChecking", "no" );
        sshConfig.put ( "PreferredAuthentications", "password" );
        sshSession.setConfig ( sshConfig );
        sshSession.connect ();
        Channel channel = sshSession.openChannel ( "sftp" );
        channel.connect ();
        sftp = (ChannelSftp) channel;
        return sftp;
    }

    /**
     * download file by sftp.
     *
     * @param downloadFile
     * @param saveFile
     * @param sftp
     * @return
     * @throws Exception
     */
    public static File download(final String downloadFile, final String saveFile, final ChannelSftp sftp)
            throws Exception {
        FileOutputStream os = null;
        File file = new File ( saveFile );
        try {
            if (!file.exists ()) {
                File parentFile = file.getParentFile ();
                if (!parentFile.exists ()) {
                    parentFile.mkdirs ();
                }
                file.createNewFile ();
            }
            os = new FileOutputStream ( file );
            List<String> list = formatPath ( downloadFile );
            sftp.get ( list.get ( 0 ) + list.get ( 1 ), os );
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        } finally {
            os.close ();
        }
        return file;
    }

    /**
     * download file as byte by sftp.
     *
     * @param downloadFile
     * @param sftp
     * @return
     * @throws Exception
     */
    public static byte[] downloadAsByte(final String downloadFile, final ChannelSftp sftp) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream ();
        try {
            List<String> list = formatPath ( downloadFile );
            sftp.get ( list.get ( 0 ) + list.get ( 1 ), os );
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        } finally {
            os.close ();
        }
        return os.toByteArray ();
    }

    /**
     * delete file by sftp.
     *
     * @param deleteFile
     * @param sftp
     * @throws Exception
     */
    public static void rmFile(final String deleteFile, final ChannelSftp sftp) throws Exception {
        try {
            sftp.rm ( deleteFile );
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        }
    }


    /**
     * delete dir by sftp
     *
     * @param deleteFile
     * @param sftp
     * @throws Exception
     */
    public static void rmDir(final String deleteFile, final ChannelSftp sftp) throws Exception {
        try {
            sftp.rmdir ( deleteFile );
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        }
    }

    /**
     * upload file
     *
     * @param srcFile
     * @param dir
     * @param fileName
     * @param sftp
     * @throws Exception
     */
    private static void uploadFile(final String srcFile, final String dir, final String fileName, final ChannelSftp sftp)
            throws Exception {
        mkdir ( dir, sftp );
        sftp.cd ( dir );
        sftp.put ( srcFile, fileName );
    }

    /**
     * upload file
     *
     * @param srcFile
     * @param sftp
     * @throws Exception
     */
    public static void uploadFile(final String srcFile, final ChannelSftp sftp) throws Exception {
        try {
            File file = new File ( srcFile );
            if (file.exists ()) {
                List<String> list = formatPath ( srcFile );
                uploadFile ( srcFile, list.get ( 0 ), list.get ( 1 ), sftp );
            }
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        }
    }

    /**
     * upload file
     *
     * @param srcFile
     * @param sftp
     * @throws Exception
     */
    public static void uploadFile(final String srcFile, final String location, final ChannelSftp sftp) throws Exception {
        try {
            File file = new File ( srcFile );
            if (file.exists ()) {
                List<String> list = formatPath ( srcFile );
                uploadFile ( srcFile, location, list.get ( 1 ), sftp );
            }
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        }
    }

    /**
     * create dir by dir path
     *
     * @param dir  path must be /xxx/xxx/xxx/ not be /
     * @param sftp
     * @throws Exception
     */
    public static boolean mkdir(final String dir, final ChannelSftp sftp) throws Exception {
        try {
            if (StringUtils.isBlank ( dir )) {
                return false;
            }

            String md = dir.replaceAll ( "\\\\", "/" );
            if (md.indexOf ( "/" ) != 0 || md.length () == 1) {
                return false;
            }
            return mkdirs ( md, sftp );
        } catch (Exception e) {
            exit ( sftp );
            throw e;
        }
    }

    /**
     * recursive create dir
     *
     * @param dir
     * @param sftp
     * @return
     * @throws SftpException
     */
    private static boolean mkdirs(final String dir, final ChannelSftp sftp) throws SftpException {
        String dirs = dir.substring ( 1, dir.length () );

        if (StringUtils.endsWith ( dir, "/" )) {
            dirs = dir.substring ( 0, dir.length () - 1 );
        }
        String[] dirArr = dirs.split ( "/" );
        String base = "";
        for (String d : dirArr) {
            base += "/" + d;
            if (dirExist ( base + "/", sftp )) {
                continue;
            } else {
                sftp.mkdir ( base + "/" );
            }
        }
        return true;
    }

    /**
     * Whether the dir exist
     *
     * @param dir  path : /xxx/xxx/
     * @param sftp
     * @return
     */
    public static boolean dirExist(final String dir, final ChannelSftp sftp) {
        try {
            Vector<?> vector = sftp.ls ( dir );
            if (null == vector){
                return false;
            }
            else{
                return true;
            }
        } catch (SftpException e) {
            return false;
        }
    }

    public static int getFileCount(final String dir, final ChannelSftp sftp) {
        try {
            Vector<?> vector = sftp.ls ( dir );
            int count = vector.size ();
            int result = 0;
            for (int i = 0; i < count; i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.elementAt ( i );
                String fileName = lsEntry.getFilename ();
                if (!(".".equalsIgnoreCase ( fileName ) || "..".equalsIgnoreCase ( fileName ))) {
                    result++;
                }
            }
            return result;
        } catch (SftpException e) {
            e.printStackTrace ();
        }
        return -1;
    }

    public static long getFileSize(final String filePath, final ChannelSftp sftp) {
        long result = 0;
        try {
            ByteArrayOutputStream writer = new ByteArrayOutputStream ();
            sftp.get ( filePath, writer );
            result = writer.size ();
            writer.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return result;
    }

    public static void rmFileFromDir(final String dirPath, final ChannelSftp sftp) {
        try {
            Vector<?> vector = sftp.ls ( dirPath );

            int count = vector.size ();
            for (int i = 0; i < count; i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.elementAt ( i );
                String fileName = lsEntry.getFilename ();
                if (!(".".equalsIgnoreCase ( fileName ) || "..".equalsIgnoreCase ( fileName ))) {
                    rmFile ( dirPath + "/" + fileName, sftp );
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }


    /**
     * format path
     *
     * @param srcPath original path. /xxx/xxx/xxx.yyy or  X:/xxx/xxx/xxx.yy
     * @return list, first item is path （/xxx/xxx/）,second item is file name （xxx.yy）
     */
    public static List<String> formatPath(final String srcPath) {
        List<String> list = new ArrayList<String> ( 2 );
        String dir = "";
        String fileName = "";
        String repSrc = srcPath.replaceAll ( "\\\\", "/" );
        int firstP = repSrc.indexOf ( "/" );
        int lastP = repSrc.lastIndexOf ( "/" );
        fileName = repSrc.substring ( lastP + 1 );
        dir = repSrc.substring ( firstP, lastP );
        dir = (dir.length () == 1 ? dir : (dir + "/"));
        list.add ( dir );
        list.add ( fileName );
        return list;
    }
    /**
     * close sftp
     *
     * @param sftp
     */
    public static void exit(final ChannelSftp sftp) {
        sftp.exit ();
    }
}  