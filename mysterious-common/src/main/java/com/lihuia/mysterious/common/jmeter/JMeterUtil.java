package com.lihuia.mysterious.common.jmeter;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lihuia.com
 * @date 2022/4/10 11:45 AM
 */

@Component
public class JMeterUtil {

    /** master节点用例，报告目录 */
    public final static String MYSTERIOUS_DATA_HOME = "MYSTERIOUS_DATA_HOME";

    /** master节点Jmeter路径 */
    public final static String MASTER_JMETER_HOME = "MASTER_JMETER_HOME";

    /** master节点Jmeter执行目录 */
    public final static String MASTER_JMETER_HOME_BIN = "MASTER_JMETER_HOME_BIN";

    /** slave节点Jmeter路径 */
    public final static String SLAVE_JMETER_HOME = "SLAVE_JMETER_HOME";

    /** slave节点Jmeter-Server执行目录 */
    public final static String SLAVE_JMETER_HOME_BIN = "SLAVE_JMETER_HOME_BIN";

    /** slave节点Jmeter-Server日志目录 */
    public final static String SLAVE_JMETER_HOME_LOG = "SLAVE_JMETER_HOME_LOG";

    /** master节点测试计划用户上传的测试报告存储路径 */
    public final static String MASTER_REPORT_FILES_PATH = "MASTER_REPORT_FILES_PATH";

    /** 在线生成JMeter脚本，基于该目录下的jmx脚本来生成 */
    public final static String MASTER_BASE_JMX_FILES_PATH = "MASTER_BASE_JMX_FILES_PATH";

    /** 用户jmeter脚本里csv的Name有没有改成上传的csv文件同名，0未改，报错；1修改了，正确 */
    private int csvFlag = 0;

    public String read(String filePath, String regex, String replaceContent) {

        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();

        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
                Matcher matcher = Pattern.compile(regex).matcher(line);
                if (matcher.find() && !StringUtils.isEmpty(matcher.group(1))) {
                    buf.append(line.replace(matcher.group(1), replaceContent));
                }
                // 如果不用修改, 则按原来的内容回写
                else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }

        return buf.toString();
    }

    public void write(String filePath, String content) {
        BufferedWriter bw = null;

        try {
            // 根据文件路径创建缓冲输出流
            bw = new BufferedWriter(new FileWriter(filePath));
            // 将内容写入文件中
            bw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
    }

    public Document initDocument(String jmxFilePath) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new File(jmxFilePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (null == document) {
            throw new MysteriousException(ResponseCodeEnum.JMX_ERROR);
        }
        return document;
    }

    /**
     * 上传JMX，拷贝一份debug脚本，然后修改线程数都为1，使得debug操作只执行一次
     * @param jmxFilePath
     */
    public void updateDebugThread(String jmxFilePath) {
        Document document = initDocument(jmxFilePath);
        Element root = document.getRootElement();
        updateThread(jmxFilePath, document, root);
    }

    /**
     * 更新csv文件节点，根据上传csv文件寻找jmx里节点处，更新为新的csv依赖路径
     * @param jmxFilePath
     * @param csvFile
     * @param csvFilePath
     */
    public void updateJmxCsvFilePath(String jmxFilePath, String csvFile, String csvFilePath) {
        Document document = initDocument(jmxFilePath);
        Element root = document.getRootElement();
        updateCSVFilePath(jmxFilePath, document, root, csvFile, csvFilePath);
    }

    /**
     * 更新jar文件路径，寻找jmx里classpath节点处，更新为新的jar路径
     * @param jmxFilePath
     * @param jarFilePath
     */
    public void updateJmxJarFilePath(String jmxFilePath, String jarFilePath) {
        Document document = initDocument(jmxFilePath);
        Element root = document.getRootElement();
        updateJARFilePath(jmxFilePath, document, root, jarFilePath);
    }

    /**
     * 更新JMX文件，具体的写操作
     * @param document
     * @param jmxFilePath
     */
    private void writeXML(Document document, String jmxFilePath) {
        //OutputFormat format = OutputFormat.createPrettyPrint();
        OutputFormat format = new OutputFormat();
        try {
            /** 中文乱码了 */
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            FileOutputStream output = new FileOutputStream(jmxFilePath);

            XMLWriter writer = new XMLWriter(output, format);
            writer.write(document);
            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历JMX，修改线程数等参数
     * 原始的ThreadGroup：
     *           <stringProp name="LoopController.loops">1</stringProp>
     *           <stringProp name="ThreadGroup.num_threads">1</stringProp>
     *           <stringProp name="ThreadGroup.ramp_time">1</stringProp>
     * SteppingThreadGroup：
     *         <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
     *         <stringProp name="ThreadGroup.num_threads">100</stringProp>
     *         <stringProp name="Threads initial delay">0</stringProp>
     *         <stringProp name="Start users count">5</stringProp>
     *         <stringProp name="Start users count burst">5</stringProp>
     *         <stringProp name="Start users period">60</stringProp>
     *         <stringProp name="Stop users count">100</stringProp>
     *         <stringProp name="Stop users period">0</stringProp>
     *         <stringProp name="flighttime">60</stringProp>
     *         <stringProp name="rampUp">0</stringProp>
     * ConcurrencyThreadGroup：
     *         <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
     *         <stringProp name="TargetLevel">10</stringProp>
     *         <stringProp name="RampUp">600</stringProp>
     *         <stringProp name="Steps">10</stringProp>
     *         <stringProp name="Hold">600</stringProp>
     *         <stringProp name="LogFilename"></stringProp>
     *         <stringProp name="Iterations"></stringProp>
     *         <stringProp name="Unit">S</stringProp>
     * @param jmxFilePath
     * @param document
     * @param root
     */
    private void updateThread(String jmxFilePath, Document document, Element root) {
        String steppingThreadGroup = "kg.apc.jmeter.threads.SteppingThreadGroup";
        String concurrencyThreadGroup = "com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup";

        for (Element element : root.elements()) {
            //线程组插件1：原生默认ThreadGroup
            //线程组插件2：SteppingTrheadGroup
            //线程组插件3：bzm里的ConcurrencyThreadGroup
            if ("ThreadGroup".equals(element.getName())
                    && "ThreadGroup".equals(element.attributeValue("testclass"))
                    //线程组必须不能是disable，要么为true，要么没有
                    && !"false".equals(element.attributeValue("enabled"))) {
                for (Element prop : element.elements()) {
                    System.out.println(prop.getName());
                    if ("elementProp".equals(prop.getName()) &&
                            "ThreadGroup.main_controller".equals(prop.attributeValue("name"))) {
                        for (Element loopProp : prop.elements()) {
                            if ("LoopController.loops".equals(loopProp.attributeValue("name"))) {
                                loopProp.setText("1");
                                writeXML(document, jmxFilePath);
                            }
                        }
                    }
                    if ("ThreadGroup.num_threads".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("ThreadGroup.ramp_time".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                }
                //有可能有多个ThreadGroup，不能break
                //break;
            } else if (steppingThreadGroup.equals(element.getName())
                    && steppingThreadGroup.equals(element.attributeValue("testclass"))
                    && !"false".equals(element.attributeValue("enabled"))) {
                for (Element prop : element.elements()) {
                    if ("ThreadGroup.num_threads".equals(prop.attributeValue("name"))) {
                        prop.setText("2");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Threads initial delay".equals(prop.attributeValue("name"))) {
                        prop.setText("0");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Start users count".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Start users count burst".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Start users period".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Stop users count".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Stop users period".equals(prop.attributeValue("name"))) {
                        prop.setText("0");
                        writeXML(document, jmxFilePath);
                    }
                    if ("flighttime".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("rampUp".equals(prop.attributeValue("name"))) {
                        prop.setText("0");
                        writeXML(document, jmxFilePath);
                    }
                    if ("elementProp".equals(prop.getName()) &&
                            "ThreadGroup.main_controller".equals(prop.attributeValue("name"))) {
                        for (Element loopProp : prop.elements()) {
                            if ("LoopController.loops".equals(loopProp.attributeValue("name"))) {
                                loopProp.setText("1");
                                writeXML(document, jmxFilePath);
                            }
                        }
                    }
                }
            } else if (concurrencyThreadGroup.equals(element.getName())
                    && concurrencyThreadGroup.equals(element.attributeValue("testclass"))
                    && !"false".equals(element.attributeValue("enabled"))) {

                for (Element prop : element.elements()) {
                    if ("TargetLevel".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("RampUp".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Steps".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Hold".equals(prop.attributeValue("name"))) {
                        prop.setText("1");
                        writeXML(document, jmxFilePath);
                    }
                    if ("Unit".equals(prop.attributeValue("name"))) {
                        prop.setText("S");
                        writeXML(document, jmxFilePath);
                    }
                }
            } else {
                updateThread(jmxFilePath, document, element);
            }
        }
    }

    /**
     * 遍历csv文件节点，查找是否有testname和上传的csv同名的（按约定）
     * @param jmxFilePath
     * @param csvFile
     */
    public boolean existCSVFileName(String jmxFilePath, String csvFile) {
        Document document = initDocument(jmxFilePath);
        Element root = document.getRootElement();
        checkCSVFileName(root, csvFile);
        return csvFlag == 1;
    }

    /**
     * 遍历上传的csv文件，是否存在启用的csv节点，testname命名为上传的文件名
     * @param root
     * @param csvFile
     * @return
     */
    private void checkCSVFileName(Element root, String csvFile) {
        for (Element element : root.elements()) {
            if ("CSVDataSet".equals(element.getName())
                    && csvFile.equals(element.attributeValue("testname"))
                    && !"false".equals(element.attributeValue("enabled"))) {
                csvFlag = 1;
            } else {
                checkCSVFileName(element, csvFile);
            }
        }
    }

    /**
     * 递归遍历JMX脚本xml文件，找到CSV节点处，更新上传的csv文件路径
     * @param jmxFilePath
     * @param document
     * @param root
     * @param csvFile
     * @param csvFilePath
     */
    private void updateCSVFilePath(String jmxFilePath, Document document,
                                   Element root, String csvFile, String csvFilePath) {

        for (Element element : root.elements()) {
            /** jmx脚本文件的CSV配置节点 */
            if ("CSVDataSet".equals(element.getName())
                    && csvFile.equals(element.attributeValue("testname"))
                    && !"false".equals(element.attributeValue("enabled"))) {
                for (Element prop : element.elements()) {
                    if ("filename".equals(prop.attributeValue("name"))) {
                        prop.setText(csvFilePath);
                        //System.out.println(prop.getText());
                        writeXML(document, jmxFilePath);
                    }
                }
                //每次上传是一个csv，但是有可能jmx多个地方用到这个csv，那么enable的都必须修改，不能break
                //break;
            } else {
                /** 递归找到对应的csv文件所在的地方 */
                updateCSVFilePath(jmxFilePath, document, element, csvFile, csvFilePath);
            }
        }
    }

    /**
     * 递归遍历JMX脚本xml文件，找到JAR的classpath节点处，更新上传的jar路径
     * @param jmxFilePath
     * @param document
     * @param root
     * @param jarFilePath
     */
    public void updateJARFilePath(String jmxFilePath, Document document, Element root, String jarFilePath) {

        for (Element element : root.elements()) {
            /** jmx脚本文件的CSV配置节点 */
            if ("TestPlan".equals(element.getName())) {
                for (Element prop : element.elements()) {
                    if ("TestPlan.user_define_classpath".equals(prop.attributeValue("name"))) {
                        prop.setText(jarFilePath);
                        writeXML(document, jmxFilePath);
                    }
                }
                break;
            } else {
                updateJARFilePath(jmxFilePath, document, element, jarFilePath);
            }
        }
    }



    public void getResponseData(String xmlFilePath) {
        Document document = initDocument(xmlFilePath);
        Element root = document.getRootElement();
        for (Element element : root.elements()) {
            if ("httpSample".equals(element.getName())) {
                for (Element responseData : element.elements()) {
                    if ("responseData".equals(responseData.getName()) &&
                            "java.lang.String".equals(responseData.attributeValue("class"))) {
                        System.out.println(responseData.getText());
                    }
                }
            }
        }
    }

    public boolean isMatched(String filePath, String regex) {
        BufferedReader br = null;
        String line = null;

        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
                Matcher matcher = Pattern.compile(regex).matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
            return false;
        }
    }

    private Long getFileSize(String filePath) {
        FileChannel fc= null;
        try {
            File f= new File(filePath);
            if (f.exists() && f.isFile()){
                FileInputStream fis= new FileInputStream(f);
                fc= fis.getChannel();
                return fc.size();
            }else{
                System.out.println("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        JMeterUtil jmeter = new JMeterUtil();

    }
}
