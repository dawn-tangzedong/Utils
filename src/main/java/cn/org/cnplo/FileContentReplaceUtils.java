package cn.org.cnplo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangzedong.programmer@gamil.com
 * @apiNote 文件内容替换工具
 * @since 2021/1/24 00:19
 */
public class FileContentReplaceUtils {



    public static void main(String[] args) {
        getFile(new File("/Users/tangzedong/dev/project/TangZeDong/generator/src/main/java/plo/utils/generate.copy"));
    }

    /**
     * 是否替换文件的名称，替换可能导致产生新的文件
     */
    static final boolean REPLACE_FILE_NAME = true;

    /**
     * 是否删除原本文件
     */
    static final boolean REMOVE_OLD_FILE  = true;

    /**
     * 替换的文本内容
     */
    static HashMap<String, String> REPLACE_CONTENT = new HashMap<String, String>() {{
        // put("content", "replace_content");
    }};

    /**
     * 获取文件信息
     * @param path 文件路径
     */
    public static void getFile(String path){
        getFile(new File(path));
    }

    /**
     * 获取文件信息
     * @param directory 文件
     */
    public static void getFile(File directory) {
        // 文件不存在退出
        if (!directory.exists()) {
            return;
        }
        // 判断文件是否是文件夹
        if (directory.isDirectory()) {
            // 该文件是文件夹类型
            File[] children = directory.listFiles();
            if (children == null || children.length == 0) {
                return;
            }
            for (File child : children) {
                getFile(child);
            }
        } else {
            // 文件处理，替换文件中的文本内容
            replaceContent(directory);
        }
    }

    /**
     * 替换文件中的文本内容
     *
     * 注： 若文件名称中存在替换的文本，则会将原本文件删除，生成新的文件
     * @param file 文件信息
     */
    public static void replaceContent(File file) {
        try {
            //数据流读取文件
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder strBuffer = new StringBuilder();
            String temp;
            while ((temp = bufReader.readLine()) != null) {
                //判断当前行是否存在想要替换掉的字符 -1表示存在
                strBuffer.append(replaceContent(temp));
                strBuffer.append(System.getProperty("line.separator"));
            }
            bufReader.close();
            // 删除原文件
            if (REMOVE_OLD_FILE) {
                file.delete();
            }
            //替换后输出的文件位置
            PrintWriter printWriter = new PrintWriter(
                    REPLACE_FILE_NAME ? replaceContent(file.getAbsolutePath()) : file.getAbsolutePath()
            );
            printWriter.write(strBuffer.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换文本中的内容
     * @param content 文本内容
     * @return 返回替换后的结果
     */
    private static String replaceContent(String content) {
        for (Map.Entry<String, String> entry : REPLACE_CONTENT.entrySet()) {
            if (content.contains(entry.getKey())) {
                content = content.replaceAll(entry.getKey(), entry.getValue());
            }
        }
        return content;
    }
}
