package com.wen.tools.io.domain;

public interface ToolsIOIConstants {
    interface CSV {
        //转义字符, 默认是双引号
        char DEFAULT_ESCAPE_CHARACTER = '\\';
        //分隔符
        char DEFAULT_SEPARATOR = ',';
        //引号字符，双引号或者单引号，默认双引号，每个值都使用指定的引号包围起来，即使数据类型是int类型也会用引号引起来
        //\u0000 空
        char DEFAULT_QUOTE_CHARACTER ='"';
        String DEFAULT_LINE_END="\n";
    }
}
