package com.wen.pool;

import com.wen.pool.bean.SessionFactory;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * @author : WChen129
 * @date : 2018-07-10
 */
public class test {
    @Test
    public void testSessionFactory() throws Exception {
        SessionFactory sessionFactory = SessionFactory.getSessionFactory();
        FileReader fr = new FileReader("src/test/resources/url.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        String[] arrs = null;
        while ((line = br.readLine()) != null) {
            String id = line.replace("https://blog.csdn.net/u012957549/article/details/", "");
            UrlEntry urlEntry = new UrlEntry();
            urlEntry.setId(id);
            urlEntry.setUrl(line.trim());
            urlEntry.set_delete_("0");
            urlEntry.setOriFrom("csdn");
            String sql="INSERT INTO url_table(id,url) VALUE('" + id + "','" + line + "')";
            System.out.println(sql);
            int value = sessionFactory.executeUpdate(sql, null);
            System.out.println(value);
        }
        br.close();
        fr.close();
    }
}
