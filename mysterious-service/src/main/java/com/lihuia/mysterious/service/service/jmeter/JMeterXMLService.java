package com.lihuia.mysterious.service.service.jmeter;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ThreadGroupDO;
import com.lihuia.mysterious.service.enums.JMeterSampleEnum;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2024/9/6 10:23
 */

@Slf4j
@Component
public class JMeterXMLService {

    private Document document;

    private Element dest;

    private int flag = 0;

    public void init(String srcJmxFilePath) {
        log.info("init {}", srcJmxFilePath);
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(new File(srcJmxFilePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (null == document) {
            throw new MysteriousException(ResponseCodeEnum.JMX_ERROR);
        }
    }

    private void initFlag() {
        this.flag = 0;
    }

    private void findElement(Element root, String name) {
        if (flag == 1) {
            return;
        }
        for (Element element : root.elements()) {
            //System.out.println(element.getName());
            if (element.getName().equals(name)) {
                dest = element;
                flag = 1;
                return;
            } else {
                findElement(element, name);
            }
        }
    }

    public void addHttpHeader(String name, String value) {
        log.info("{}=>{}", name, value);
        Element collectionProp =
                document.getRootElement()
                        .element("hashTree")
                        .element("hashTree")
                        .element("HeaderManager")
                        .element("collectionProp");
        Element elementProp = collectionProp.addElement("elementProp");
        elementProp.addAttribute("name", "");
        elementProp.addAttribute("elementType", "Header");

        Element headerName = elementProp.addElement("stringProp");
        headerName.addAttribute("name", "Header.name");
        headerName.setText(name);

        Element headerValue = elementProp.addElement("stringProp");
        headerValue.addAttribute("name", "Header.value");
        headerValue.setText(value);
    }

    public void cleanHttpHeader() {
        Element collectionProp =
                document.getRootElement()
                        .element("hashTree")
                        .element("hashTree")
                        .element("HeaderManager")
                        .element("collectionProp");
        if (!CollectionUtils.isEmpty(collectionProp.elements())) {
            collectionProp.elements().forEach(element -> collectionProp.remove(element));
        }
    }

    public void cleanHttpParam() {
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");
        Element collectionProp = dest.element("elementProp").element("collectionProp");
        if (!CollectionUtils.isEmpty(collectionProp.elements())) {
            collectionProp.elements().forEach(element -> collectionProp.remove(element));
        }
    }

    public void addHttpParam(String name, String value) {
        /** hashTree这种节点名太多了，一层一层遍历太麻烦了，直接递归找到对应的节点
         * 但是有一个问题，假如脚本里有多个HTTP Sample，就会有重复的
         * 到时候再说~~*/
        log.info("{}=>{}", name, value);
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");
        Element collectionProp = dest.element("elementProp").element("collectionProp");


        Element elementProp = collectionProp.addElement("elementProp");
        elementProp.addAttribute("name", name);
        elementProp.addAttribute("elementType", "HTTPArgument");

        Element alwaysEncode = elementProp.addElement("boolProp");
        alwaysEncode.addAttribute("name", "HTTPArgument.always_encode");
        alwaysEncode.setText("true");

        Element argumentValue = elementProp.addElement("stringProp");
        argumentValue.addAttribute("name", "Argument.value");
        argumentValue.setText(value);

        Element argumentMetadata = elementProp.addElement("stringProp");
        argumentMetadata.addAttribute("name", "Argument.metadata");
        argumentMetadata.setText("=");

        Element useEquals = elementProp.addElement("boolProp");
        useEquals.addAttribute("name", "TTPArgument.use_equals");
        useEquals.setText("true");

        Element argumentName = elementProp.addElement("stringProp");
        argumentName.addAttribute("name", "Argument.name");
        argumentName.setText(name);
    }

    public void addHttpBody(String body) {
        log.info("addHttpBody: {}", body);
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");

        /** 先删除默认脚本里带的HTTP GET argument节点 */
        if (dest.element("elementProp") != null) {
            dest.remove(dest.element("elementProp"));
        }
        /** 如果boolProp存在，也删除 */
        if (dest.element("boolProp") != null) {
            dest.remove(dest.element("boolProp"));
        }

        /** 添加post节点 */
        Element boolProp = dest.addElement("boolProp");
        boolProp.addAttribute("name", "HTTPSampler.postBodyRaw");
        boolProp.setText("true");

        Element elementProp = dest.addElement("elementProp");
        elementProp.addAttribute("name", "HTTPsampler.Arguments");
        elementProp.addAttribute("elementType", "Arguments");

        Element collectionProp = elementProp.addElement("collectionProp");
        collectionProp.addAttribute("name", "Arguments.arguments");

        elementProp = collectionProp.addElement("elementProp");
        elementProp.addAttribute("name", "");
        elementProp.addAttribute("elementType", "HTTPArgument");

        boolProp = elementProp.addElement("boolProp");
        boolProp.addAttribute("name", "HTTPArgument.always_encode");
        boolProp.setText("false");

        Element stringProp = elementProp.addElement("stringProp");
        stringProp.addAttribute("name", "Argument.value");
        stringProp.setText(body);
    }

    public void updateHttpSample(HttpDO httpDO) {
        log.info("updateHttpSample: {}", httpDO);
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");

        dest.elements().forEach(element -> {
            if ("HTTPSampler.domain".equals(element.attributeValue("name"))) {
                log.info("domain=>{}", httpDO.getDomain());
                element.setText(httpDO.getDomain());
            } else if ("HTTPSampler.port".equals(element.attributeValue("name"))) {
                log.info("port=>{}", httpDO.getPort());
                element.setText(httpDO.getPort());
            } else if ("HTTPSampler.protocol".equals(element.attributeValue("name"))) {
                log.info("protocol=>{}", httpDO.getProtocol());
                element.setText(httpDO.getProtocol());
            } else if ("HTTPSampler.contentEncoding".equals(element.attributeValue("name"))) {
                log.info("contentEncoding=>{}", httpDO.getContentEncoding());
                element.setText(httpDO.getContentEncoding());
            } else if ("HTTPSampler.path".equals(element.attributeValue("name"))) {
                log.info("path=>{}", httpDO.getPath());
                element.setText(httpDO.getPath());
            } else if ("HTTPSampler.method".equals(element.attributeValue("name"))) {
                log.info("method=>{}", httpDO.getMethod());
                element.setText(httpDO.getMethod());
            } else {
                log.info("updateHttpSample, name: {}", element.attributeValue("name"));
            }
        });
    }

    public void updateJavaRequest(String javaRequestClassPath) {
        log.info("javaRequestClassPath: {}", javaRequestClassPath);
        initFlag();
        findElement(document.getRootElement(), "JavaSampler");

        dest.elements().forEach(element -> {
            if ("classname".equals(element.attributeValue("name"))) {
                log.info("classname=>{}", javaRequestClassPath);
                element.setText(javaRequestClassPath);
                return;
            }
        });
    }

    public void cleanJavaParam() {
        initFlag();
        findElement(document.getRootElement(), "JavaSampler");
        Element collectionProp = dest.element("elementProp").element("collectionProp");
        if (!CollectionUtils.isEmpty(collectionProp.elements())) {
            collectionProp.elements().forEach(element -> collectionProp.remove(element));
        }
    }

    public void addJavaParam(String name, String value) {
        log.info("addJavaParam: {}=>{}", name, value);
        initFlag();
        findElement(document.getRootElement(), "JavaSampler");
        Element collectionProp = dest.element("elementProp").element("collectionProp");

        Element elementProp = collectionProp.addElement("elementProp");
        elementProp.addAttribute("name", name);
        elementProp.addAttribute("elementType", "Argument");

        Element argumentName = elementProp.addElement("stringProp");
        argumentName.addAttribute("name", "Argument.name");
        argumentName.setText(name);

        Element argumentValue = elementProp.addElement("stringProp");
        argumentValue.addAttribute("name", "Argument.value");
        argumentValue.setText(value);

        Element argumentMetadata = elementProp.addElement("stringProp");
        argumentMetadata.addAttribute("name", "Argument.metadata");
        argumentMetadata.setText("=");
    }

    public void updateConcurrencyThreadGroup(ConcurrencyThreadGroupDO concurrencyThreadGroupDO) {
        log.info("updateConcurrencyThreadGroup: {}", concurrencyThreadGroupDO);
        initFlag();
        findElement(document.getRootElement(), "com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup");
        dest.elements().forEach(element -> {
            if ("TargetLevel".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupDO.getTargetConcurrency());
            } else if ("RampUp".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupDO.getRampUpTime());
            } else if ("Steps".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupDO.getRampUpStepsCount());
            } else if ("Hold".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupDO.getHoldTargetRateTime());
            } else {
                log.info("updateConcurrencyThreadGroup, name: {}", element.attributeValue("name"));
            }
        });
    }

    public void updateSteppingThreadGroup(SteppingThreadGroupDO steppingThreadGroupDO) {
        log.info("updateSteppingThreadGroup: {}", steppingThreadGroupDO);
        initFlag();
        findElement(document.getRootElement(), "kg.apc.jmeter.threads.SteppingThreadGroup");
        dest.elements().forEach(element -> {
            if ("ThreadGroup.num_threads".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getNumThreads());
            } else if ("Threads initial delay".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getFirstWaitForSeconds());
            } else if ("Start users count".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getNextAddThreads());
            } else if ("Start users count burst".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getThenStartThreads());
            } else if ("Start users period".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getNextAddThreadsEverySeconds());
            } else if ("Stop users count".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getFinallyStopThreads());
            } else if ("Stop users period".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getFinallyStopThreadsEverySeconds());
            } else if ("flighttime".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getThenHoldLoadForSeconds());
            } else if ("rampUp".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupDO.getUsingRampUpSeconds());
            } else {
                log.info("updateSteppingThreadGroup, name: {}", element.attributeValue("name"));
            }
        });

    }

    public void updateThreadGroup(ThreadGroupDO threadGroupDO) {
        log.info("updateThreadGroup: {}", threadGroupDO);
        initFlag();
        findElement(document.getRootElement(), "ThreadGroup");
        dest.elements().forEach(element -> {
            if ("ThreadGroup.main_controller".equals(element.attributeValue("name"))) {
                for (Element loop : element.elements()) {
                    if ("LoopController.loops".equals(loop.attributeValue("name"))) {
                        log.info("loops=>{}", threadGroupDO.getLoops());
                        loop.setText(threadGroupDO.getLoops());
                    }
                }
            } else if ("ThreadGroup.num_threads".equals(element.attributeValue("name"))) {
                log.info("numThreads=>{}", threadGroupDO.getNumThreads());
                element.setText(threadGroupDO.getNumThreads());
            } else if ("ThreadGroup.ramp_time".equals(element.attributeValue("name"))) {
                log.info("rampTime=>{}", threadGroupDO.getRampTime());
                element.setText(threadGroupDO.getRampTime());
            } else if ("ThreadGroup.scheduler".equals(element.attributeValue("name"))) {
                log.info("scheduler=>{}", threadGroupDO.getScheduler());
                log.info("修改前ThreadGroup.scheduler: {}", element.getText());
                if (threadGroupDO.getScheduler().equals(0)) {
                    element.setText("false");
                    log.info("修改后ThreadGroup.scheduler: {}", element.getText());
                }
            } else if ("ThreadGroup.duration".equals(element.attributeValue("name"))) {
                log.info("duration=>{}", threadGroupDO.getDuration());
                if (threadGroupDO.getScheduler().equals(1)
                        && !StringUtils.isEmpty(threadGroupDO.getDuration())) {
                    element.setText(threadGroupDO.getDuration());
                }
            } else if ("ThreadGroup.delay".equals(element.attributeValue("name"))) {
                log.info("delay=>{}", threadGroupDO.getDelay());
                if (threadGroupDO.getScheduler().equals(1)
                        && !StringUtils.isEmpty(threadGroupDO.getDelay())) {
                    element.setText(threadGroupDO.getDelay());
                }
            } else if ("ThreadGroup.same_user_on_next_iteration".equals(element.attributeValue("name"))) {
                log.info("same_user_on_next_iteration=>{}", threadGroupDO.getSameUserOnNextIteration());
                if (threadGroupDO.getSameUserOnNextIteration().equals(0)) {
                    element.setText("false");
                }
            } else {
                log.info("updateThreadGroup, name: {}", element.attributeValue("name"));
            }
        });
        if (null != threadGroupDO.getDelayedStart() &&
                threadGroupDO.getDelayedStart().equals(1)) {
            Element boolProp = dest.addElement("boolProp");
            boolProp.addAttribute("name", "ThreadGroup.delayedStart");
            boolProp.setText("true");
        }
    }

    public void deleteCsv(String csvFileName, Integer jmeterSampleType) {
        log.info("deleteCsv: {}", csvFileName);
        initFlag();
        if (JMeterSampleEnum.HTTP_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "HTTPSamplerProxy");
        } else if (JMeterSampleEnum.JAVA_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "JavaSampler");
        } else {
            throw new MysteriousException("目前不支持该Sampler");
        }

        Element hashTree = dest.getParent().element("hashTree");
        int hasCsv;
        //hashTree.elements().forEach(csvConfig -> {
        //for (Element element : hashTree.elements()) {
        List<Element> elementList = hashTree.elements();
        for (int i = 0; i < elementList.size(); i++) {
            Element element = elementList.get(i);
            if (element.getName().equals("CSVDataSet")
                    && element.attributeValue("testname").equals(csvFileName)) {
                log.info("deleteCsv, hashTree.remove: {}", element.getName());
                hashTree.remove(element);
                Element next = elementList.get(i + 1);
                if (next.getName().equals("hashTree")) {
                    log.info("deleteCsv, hashTree.remove: {}", next.getName());
                    hashTree.remove(next);
                }
            }
        }

    }

    public void addCsv(String csvFileName, String csvFilePath, String variableNameList, Integer jmeterSampleType) {
        log.info("addCsv: {}", csvFileName);
        initFlag();
        if (JMeterSampleEnum.HTTP_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "HTTPSamplerProxy");
        } else if (JMeterSampleEnum.JAVA_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "JavaSampler");
        } else {
            throw new MysteriousException("目前不支持该Sampler");
        }

        Element hashTree = dest.getParent().element("hashTree");

        Element cSVDataSet = hashTree.addElement("CSVDataSet");
        cSVDataSet.addAttribute("guiclass", "TestBeanGUI");
        cSVDataSet.addAttribute("testclass", "CSVDataSet");
        cSVDataSet.addAttribute("testname", csvFileName);
        cSVDataSet.addAttribute("enabled", "true");

        Element filename = cSVDataSet.addElement("stringProp");
        filename.addAttribute("name", "filename");
        filename.setText(csvFilePath);

        Element fileEncoding = cSVDataSet.addElement("stringProp");
        fileEncoding.addAttribute("name", "fileEncoding");
        fileEncoding.setText("UTF-8");

        Element variableNames = cSVDataSet.addElement("stringProp");
        variableNames.addAttribute("name", "variableNames");
        variableNames.setText(variableNameList);

        Element ignoreFirstLine = cSVDataSet.addElement("boolProp");
        ignoreFirstLine.addAttribute("name", "ignoreFirstLine");
        ignoreFirstLine.setText("true");

        Element delimiter = cSVDataSet.addElement("stringProp");
        delimiter.addAttribute("name", "delimiter");
        delimiter.setText(",");

        Element quotedData = cSVDataSet.addElement("boolProp");
        quotedData.addAttribute("name", "quotedData");
        quotedData.setText("false");

        Element recycle = cSVDataSet.addElement("boolProp");
        recycle.addAttribute("name", "recycle");
        recycle.setText("true");

        Element stopThread = cSVDataSet.addElement("boolProp");
        stopThread.addAttribute("name", "stopThread");
        stopThread.setText("false");

        Element shareMode = cSVDataSet.addElement("stringProp");
        shareMode.addAttribute("name", "shareMode");
        shareMode.setText("shareMode.all");

        /** 每个csv后面都要加一行<hashTree/> 最后一个好像不用加，但加了貌似也没问题 */
        hashTree.addElement("hashTree");
    }

    public void writeJmxFile(String destJmxFilePath) {
        log.info("writeJmxFile: {}", destJmxFilePath);
        //OutputFormat format = new OutputFormat();
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            /** 中文乱码了 */
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            FileOutputStream output = new FileOutputStream(destJmxFilePath);

            XMLWriter writer = new XMLWriter(output, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //JMeterXMLService jmx = new JMeterXMLService();
        //jmx.init("jmx/out.jmx");
        String filePath = "jmx/out.jmx";
        try {
            System.out.println(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")).readLine().replace(" ", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        jmx.addCsv("a.csv", "/home/a.csv", "a,b,c", JMeterSampleEnum.HTTP_REQUEST.getCode());
//        jmx.writeJmxFile("jmx/out.jmx");
    }
}
