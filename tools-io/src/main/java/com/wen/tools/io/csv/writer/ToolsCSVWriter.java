package com.wen.tools.io.csv.writer;

import com.alibaba.fastjson.JSON;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.wen.tools.io.domain.ToolsIOIConstants;
import com.wen.tools.io.exception.ToolsIOException;
import com.wen.tools.io.utils.FileUtils;
import com.wen.tools.log.utils.LogUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class ToolsCSVWriter {

    private char separator;
    //引号字符，
    // 双引号或者单引号，
    // 默认双引号，
    // 每个值都使用指定的引号包围起来，
    // 即使数据类型是int类型也会用引号引起来
    private char quotechar;
    //转义字符, 默认是双引号
    private char escapechar;
    private String lineEnd;
    private boolean showHeader=true;

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public ToolsCSVWriter(char separator, char quotechar, char escapechar, String lineEnd) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    public ToolsCSVWriter(char separator, char quotechar, char escapechar) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = ToolsIOIConstants.CSV.DEFAULT_LINE_END;
    }

    public ToolsCSVWriter(char separator) {
        this.separator = separator;
        this.quotechar = ToolsIOIConstants.CSV.DEFAULT_QUOTE_CHARACTER;
        this.escapechar = ToolsIOIConstants.CSV.DEFAULT_ESCAPE_CHARACTER;
        this.lineEnd = ToolsIOIConstants.CSV.DEFAULT_LINE_END;
    }

    public ToolsCSVWriter() {
        this.separator = ToolsIOIConstants.CSV.DEFAULT_SEPARATOR;
        this.quotechar = ToolsIOIConstants.CSV.DEFAULT_QUOTE_CHARACTER;
        this.escapechar = ToolsIOIConstants.CSV.DEFAULT_ESCAPE_CHARACTER;
        this.lineEnd = ToolsIOIConstants.CSV.DEFAULT_LINE_END;
    }

    public void writeCSVByListJsonString(File file, List<String> list) throws IOException {
        if (list == null) {
            LogUtil.getCoreLog().error("data is null, please check it,file path is:{}", file.getAbsoluteFile());
            throw new ToolsIOException("data is null, please check it");
        }
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (String jsonString : list) {
            resultList.add((Map<String, Object>) JSON.parse(jsonString));
        }
        writeCSVByListMap(file, resultList);
    }

    public void writeCSVByListMap(File file, List<Map<String, Object>> list) throws IOException {
        if (list == null) {
            LogUtil.getCoreLog().error("data is null, please check it,file path is:{}", file.getAbsoluteFile());
            throw new ToolsIOException("data is null, please check it");
        }
        List<String> listTemp = new ArrayList<String>();
        List<String[]> allLine = new ArrayList<String[]>();
        Map<String, Object> mapSchema = list.get(0);
        Iterator<Map.Entry<String, Object>> ite=null;

        ite  = mapSchema.entrySet().iterator();
        while (ite.hasNext()) {
            listTemp.add(ite.next().getKey());
        }
        allLine.add(listTemp.toArray(new String[listTemp.size()]));
        for (Map<String, Object> map : list) {
            listTemp.clear();
            ite = map.entrySet().iterator();
            while (ite.hasNext()) {
                listTemp.add(String.valueOf(ite.next().getValue()));
            }
            allLine.add(listTemp.toArray(new String[listTemp.size()]));
        }
        writeCSVByListArray(file, allLine);
    }

    public void writeCSVByListArray(File file, List<String[]> list) throws IOException {
        if (list == null) {
            LogUtil.getCoreLog().error("data is null, please check it,file path is:{}", file.getAbsoluteFile());
            throw new ToolsIOException("data is null, please check it");
        }
        if (file.isDirectory()) {
            LogUtil.getCoreLog().error("need a file not directory but file:{}  is a directory, please check it." + file.getAbsoluteFile());
            throw new ToolsIOException("need a file not directory but file:" + file.getAbsoluteFile() + "  is a directory, please check it.");
        } else {
            FileUtils.clearInfoForFile(file);
        }

        Writer writer = new FileWriter(file);
        ICSVWriter csvWriter = new CSVWriterBuilder(writer).withEscapeChar(escapechar)
                .withLineEnd(lineEnd)
                .withQuoteChar(quotechar)
                .withSeparator(separator)
                .build();

        csvWriter.writeAll(list);
        csvWriter.flush();
        csvWriter.close();
        writer.close();
    }
}
