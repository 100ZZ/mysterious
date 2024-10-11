package com.lihuia.mysterious.service.service.jmeter;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import com.lihuia.mysterious.core.entity.jmx.sample.http.HttpDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ConcurrencyThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.SteppingThreadGroupDO;
import com.lihuia.mysterious.core.entity.jmx.thread.ThreadGroupDO;
import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvDataVO;
import com.lihuia.mysterious.core.vo.jmx.sample.csv.CsvFileVO;
import com.lihuia.mysterious.core.vo.jmx.sample.http.HttpVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ConcurrencyThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.SteppingThreadGroupVO;
import com.lihuia.mysterious.core.vo.jmx.thread.ThreadGroupVO;
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
import org.apache.commons.lang3.StringUtils;

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

    public void addAssertion(Integer jmeterSampleType, String responseCode, String responseMessage, String jsonPath, String expectedValue) {
        log.info("Adding assertions: ResponseCode={}, ResponseMessage={}, JSONPath={}, ExpectedValue={}",
                responseCode, responseMessage, jsonPath, expectedValue);
        initFlag();

        // 根据类型找到目标节点（HTTP, Java, Dubbo请求）
        if (JMeterSampleEnum.HTTP_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "HTTPSamplerProxy");
        } else if (JMeterSampleEnum.JAVA_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "JavaSampler");
        } else if (JMeterSampleEnum.DUBBO_SAMPLE.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "DubboSampler");
        } else {
            log.error("jmeterSampleType异常: {}", jmeterSampleType);
            throw new MysteriousException("jmeterSampleType异常:" + jmeterSampleType);
        }

        // 确保目标节点存在
        if (dest == null) {
            log.error("未找到合适的采样器节点");
            throw new MysteriousException("未找到合适的采样器节点");
        }

        // 找到当前采样器的父节点并获取其 hashTree
        Element parent = dest.getParent();
        Element hashTree = parent.element("hashTree");

        // 如果 hashTree 不存在，则记录日志并返回
        if (hashTree == null) {
            log.error("hashTree节点不存在");
            throw new MysteriousException("hashTree节点不存在");
        }

        if (StringUtils.isNotBlank(responseCode)) {
            // 添加Response Code断言
            Element responseCodeAssertion = hashTree.addElement("ResponseAssertion");
            responseCodeAssertion.addAttribute("guiclass", "AssertionGui");
            responseCodeAssertion.addAttribute("testclass", "ResponseAssertion");
            responseCodeAssertion.addAttribute("testname", "ResponseCodeAssertion");
            responseCodeAssertion.addAttribute("enabled", "true");

            Element collectionProp = responseCodeAssertion.addElement("collectionProp");
            collectionProp.addAttribute("name", "Asserion.test_strings");

            Element stringProp = collectionProp.addElement("stringProp");
            stringProp.addAttribute("name", "49586");
            stringProp.setText(responseCode);

            Element customMessage = responseCodeAssertion.addElement("stringProp");
            customMessage.addAttribute("name", "Assertion.custom_message");

            Element testField = responseCodeAssertion.addElement("stringProp");
            testField.addAttribute("name", "Assertion.test_field");
            testField.setText("Assertion.response_code");

            Element assumeSuccess = responseCodeAssertion.addElement("boolProp");
            assumeSuccess.addAttribute("name", "Assertion.assume_success");
            assumeSuccess.setText("false");

            Element testType = responseCodeAssertion.addElement("intProp");
            testType.addAttribute("name", "Assertion.test_type");
            testType.setText("8");

            // 添加 <hashTree/>
            hashTree.addElement("hashTree");
        }

        if (StringUtils.isNotBlank(responseMessage)) {
            // 添加Response Message断言
            Element responseMessageAssertion = hashTree.addElement("ResponseAssertion");
            responseMessageAssertion.addAttribute("guiclass", "AssertionGui");
            responseMessageAssertion.addAttribute("testclass", "ResponseAssertion");
            responseMessageAssertion.addAttribute("testname", "ResponseMessageAssertion");
            responseMessageAssertion.addAttribute("enabled", "true");

            Element collectionPropMessage = responseMessageAssertion.addElement("collectionProp");
            collectionPropMessage.addAttribute("name", "Asserion.test_strings");

            Element stringPropMessage = collectionPropMessage.addElement("stringProp");
            stringPropMessage.addAttribute("name", "789079806");
            stringPropMessage.setText(responseMessage);

            Element customMessageAssertion = responseMessageAssertion.addElement("stringProp");
            customMessageAssertion.addAttribute("name", "Assertion.custom_message");

            Element testFieldMessage = responseMessageAssertion.addElement("stringProp");
            testFieldMessage.addAttribute("name", "Assertion.test_field");
            testFieldMessage.setText("Assertion.response_data");

            Element assumeSuccessMessage = responseMessageAssertion.addElement("boolProp");
            assumeSuccessMessage.addAttribute("name", "Assertion.assume_success");
            assumeSuccessMessage.setText("false");

            Element testTypeMessage = responseMessageAssertion.addElement("intProp");
            testTypeMessage.addAttribute("name", "Assertion.test_type");
            testTypeMessage.setText("2");

            // 添加 <hashTree/>
            hashTree.addElement("hashTree");
        }

        if (StringUtils.isNotBlank(expectedValue)) {
            /** 坑爹的，如果json path的value为空，jmeter会报错;所以非空才添加控件 */
            // 添加JSON Path断言
            Element jsonPathAssertion = hashTree.addElement("JSONPathAssertion");
            jsonPathAssertion.addAttribute("guiclass", "JSONPathAssertionGui");
            jsonPathAssertion.addAttribute("testclass", "JSONPathAssertion");
            jsonPathAssertion.addAttribute("testname", "JSONAssertion");
            jsonPathAssertion.addAttribute("enabled", "true");

            Element jsonPathProp = jsonPathAssertion.addElement("stringProp");
            jsonPathProp.addAttribute("name", "JSON_PATH");
            jsonPathProp.setText(jsonPath);

            Element expectedValueProp = jsonPathAssertion.addElement("stringProp");
            expectedValueProp.addAttribute("name", "EXPECTED_VALUE");
            expectedValueProp.setText(expectedValue);

            Element jsonValidation = jsonPathAssertion.addElement("boolProp");
            jsonValidation.addAttribute("name", "JSONVALIDATION");
            jsonValidation.setText("true");

            Element expectNull = jsonPathAssertion.addElement("boolProp");
            expectNull.addAttribute("name", "EXPECT_NULL");
            expectNull.setText("false");

            Element invert = jsonPathAssertion.addElement("boolProp");
            invert.addAttribute("name", "INVERT");
            invert.setText("false");

            Element isRegex = jsonPathAssertion.addElement("boolProp");
            isRegex.addAttribute("name", "ISREGEX");
            isRegex.setText("false");

            // 添加 <hashTree/>
            hashTree.addElement("hashTree");
        }

        log.info("Assertions added successfully.");
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
        useEquals.addAttribute("name", "HTTPArgument.use_equals");
        useEquals.setText("true");

        Element argumentName = elementProp.addElement("stringProp");
        argumentName.addAttribute("name", "Argument.name");
        argumentName.setText(name);
    }

    public void deleteByElement(Element now, String prop) {
        if (now.element(prop) != null) {
            now.remove(now.element(prop));
        }
    }

    public void deleteByAttribute(Element now, String prop, String name) {
        if (now.element(prop) != null) {
            Element nowProp = now.element(prop);
            /** 如果<boolProp name="HTTPSampler.postBodyRaw"></boolProp> 删除 */
            if (nowProp != null && nowProp.attributeValue("name").equals(name)) {
                now.remove(nowProp);
            }
        }
    }

    public void addHttpBody(String body) {
        log.info("addHttpBody: {}", body);
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");

        /** 先删除默认脚本里带的HTTP argument节点 */
        /** GET:  <elementProp name="HTTPsampler.Arguments" elementType="Arguments"
         *          guiclass="HTTPArgumentsPanel" testclass="Arguments"
         *          testname="User Defined Variables" enabled="true">
         *  POST: <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
         */
        deleteByElement(dest, "elementProp");

        /** 如果boolProp存在，也删除 */
        /** POST(仅仅)： <boolProp name="HTTPSampler.postBodyRaw">true</boolProp>
         */
        deleteByAttribute(dest, "boolProp", "HTTPSampler.postBodyRaw");

        /** 添加post节点 */
        Element httpPostBoolProp = dest.addElement("boolProp");
        httpPostBoolProp.addAttribute("name", "HTTPSampler.postBodyRaw");
        httpPostBoolProp.setText("true");

        Element httpSamplerArgElementProp = dest.addElement("elementProp");
        httpSamplerArgElementProp.addAttribute("name", "HTTPsampler.Arguments");
        httpSamplerArgElementProp.addAttribute("elementType", "Arguments");

        Element argumentsCollectionProp = httpSamplerArgElementProp.addElement("collectionProp");
        argumentsCollectionProp.addAttribute("name", "Arguments.arguments");

        Element httpArgElementProp = argumentsCollectionProp.addElement("elementProp");
        httpArgElementProp.addAttribute("name", "");
        httpArgElementProp.addAttribute("elementType", "HTTPArgument");

        Element alwaysEncodeBoolProp = httpArgElementProp.addElement("boolProp");
        alwaysEncodeBoolProp.addAttribute("name", "HTTPArgument.always_encode");
        alwaysEncodeBoolProp.setText("false");

        Element arguValueStringProp = httpArgElementProp.addElement("stringProp");
        arguValueStringProp.addAttribute("name", "Argument.value");
        arguValueStringProp.setText(body);

    }

    public void updateHttpSample(HttpVO httpVO) {
        log.info("updateHttpSample: {}", httpVO);
        initFlag();
        findElement(document.getRootElement(), "HTTPSamplerProxy");

        dest.elements().forEach(element -> {
            if ("HTTPSampler.domain".equals(element.attributeValue("name"))) {
                log.info("domain=>{}", httpVO.getDomain());
                element.setText(httpVO.getDomain());
            } else if ("HTTPSampler.port".equals(element.attributeValue("name"))) {
                log.info("port=>{}", httpVO.getPort());
                element.setText(httpVO.getPort());
            } else if ("HTTPSampler.protocol".equals(element.attributeValue("name"))) {
                log.info("protocol=>{}", httpVO.getProtocol());
                element.setText(httpVO.getProtocol());
            } else if ("HTTPSampler.contentEncoding".equals(element.attributeValue("name"))) {
                log.info("contentEncoding=>{}", httpVO.getContentEncoding());
                element.setText(httpVO.getContentEncoding());
            } else if ("HTTPSampler.path".equals(element.attributeValue("name"))) {
                log.info("path=>{}", httpVO.getPath());
                element.setText(httpVO.getPath());
            } else if ("HTTPSampler.method".equals(element.attributeValue("name"))) {
                log.info("method=>{}", httpVO.getMethod());
                element.setText(httpVO.getMethod());
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

    public void updateConcurrencyThreadGroup(ConcurrencyThreadGroupVO concurrencyThreadGroupVO) {
        log.info("updateConcurrencyThreadGroup: {}", concurrencyThreadGroupVO);
        initFlag();
        findElement(document.getRootElement(), "com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup");
        dest.elements().forEach(element -> {
            if ("TargetLevel".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupVO.getTargetConcurrency());
            } else if ("RampUp".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupVO.getRampUpTime());
            } else if ("Steps".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupVO.getRampUpStepsCount());
            } else if ("Hold".equals(element.attributeValue("name"))) {
                element.setText(concurrencyThreadGroupVO.getHoldTargetRateTime());
            } else {
                log.info("updateConcurrencyThreadGroup, name: {}", element.attributeValue("name"));
            }
        });
    }

    public void updateSteppingThreadGroup(SteppingThreadGroupVO steppingThreadGroupVO) {
        log.info("updateSteppingThreadGroup: {}", steppingThreadGroupVO);
        initFlag();
        findElement(document.getRootElement(), "kg.apc.jmeter.threads.SteppingThreadGroup");
        dest.elements().forEach(element -> {
            if ("ThreadGroup.num_threads".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getNumThreads());
            } else if ("Threads initial delay".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getFirstWaitForSeconds());
            } else if ("Start users count".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getNextAddThreads());
            } else if ("Start users count burst".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getThenStartThreads());
            } else if ("Start users period".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getNextAddThreadsEverySeconds());
            } else if ("Stop users count".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getFinallyStopThreads());
            } else if ("Stop users period".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getFinallyStopThreadsEverySeconds());
            } else if ("flighttime".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getThenHoldLoadForSeconds());
            } else if ("rampUp".equals(element.attributeValue("name"))) {
                element.setText(steppingThreadGroupVO.getUsingRampUpSeconds());
            } else {
                log.info("updateSteppingThreadGroup, name: {}", element.attributeValue("name"));
            }
        });

    }

    public void updateThreadGroup(ThreadGroupVO threadGroupVO) {
        log.info("updateThreadGroup: {}", threadGroupVO);
        initFlag();
        findElement(document.getRootElement(), "ThreadGroup");
        dest.elements().forEach(element -> {
            if ("ThreadGroup.main_controller".equals(element.attributeValue("name"))) {
                for (Element loop : element.elements()) {
                    if ("LoopController.loops".equals(loop.attributeValue("name"))) {
                        log.info("loops=>{}", threadGroupVO.getLoops());
                        loop.setText(threadGroupVO.getLoops());
                    }
                }
            } else if ("ThreadGroup.num_threads".equals(element.attributeValue("name"))) {
                log.info("numThreads=>{}", threadGroupVO.getNumThreads());
                element.setText(threadGroupVO.getNumThreads());
            } else if ("ThreadGroup.ramp_time".equals(element.attributeValue("name"))) {
                log.info("rampTime=>{}", threadGroupVO.getRampTime());
                element.setText(threadGroupVO.getRampTime());
            } else if ("ThreadGroup.scheduler".equals(element.attributeValue("name"))) {
                log.info("scheduler=>{}", threadGroupVO.getScheduler());
                log.info("修改前ThreadGroup.scheduler: {}", element.getText());
                if (threadGroupVO.getScheduler().equals(0)) {
                    element.setText("false");
                    log.info("修改后ThreadGroup.scheduler: {}", element.getText());
                }
            } else if ("ThreadGroup.duration".equals(element.attributeValue("name"))) {
                log.info("duration=>{}", threadGroupVO.getDuration());
                if (threadGroupVO.getScheduler().equals(1)
                        && !StringUtils.isBlank(threadGroupVO.getDuration())) {
                    element.setText(threadGroupVO.getDuration());
                }
            } else if ("ThreadGroup.delay".equals(element.attributeValue("name"))) {
                log.info("delay=>{}", threadGroupVO.getDelay());
                if (threadGroupVO.getScheduler().equals(1)
                        && !StringUtils.isBlank(threadGroupVO.getDelay())) {
                    element.setText(threadGroupVO.getDelay());
                }
            } else if ("ThreadGroup.same_user_on_next_iteration".equals(element.attributeValue("name"))) {
                log.info("same_user_on_next_iteration=>{}", threadGroupVO.getSameUserOnNextIteration());
                if (threadGroupVO.getSameUserOnNextIteration().equals(0)) {
                    element.setText("false");
                }
            } else {
                log.info("updateThreadGroup, name: {}", element.attributeValue("name"));
            }
        });
        if (null != threadGroupVO.getDelayedStart() &&
                threadGroupVO.getDelayedStart().equals(1)) {
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

    public void addCsv(CsvDataVO csvDataVO, Integer jmeterSampleType) {
        log.info("addCsv: {}", csvDataVO);
        initFlag();
        if (JMeterSampleEnum.HTTP_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "HTTPSamplerProxy");
        } else if (JMeterSampleEnum.JAVA_REQUEST.getCode().equals(jmeterSampleType)) {
            findElement(document.getRootElement(), "JavaSampler");
        } else {
            throw new MysteriousException("目前不支持该Sampler");
        }

        Element hashTree = dest.getParent().element("hashTree");

        /** 多个csv文件 */
        List<CsvFileVO> csvFileVOList = csvDataVO.getCsvFileVOList();
        for (CsvFileVO csvFileVO : csvFileVOList) {
            String filename = csvFileVO.getFilename();
            String variableNames = csvFileVO.getVariableNames();
            String delimiter = csvFileVO.getDelimiter();
            String fileEncoding = csvDataVO.getFileEncoding();
            String ignoreFirstLine = csvDataVO.getIgnoreFirstLine() == 1 ? "true" : "false";
            String allowQuotedData = csvDataVO.getAllowQuotedData() == 1 ? "true" : "false";
            String recycleOnEof = csvDataVO.getRecycleOnEof() == 1 ? "true" : "false";
            String stopThreadOnEof = csvDataVO.getStopThreadOnEof() == 1 ? "true" : "false";
            //<stringProp name="shareMode">shareMode.group</stringProp>
            String sharingModeWeb = csvDataVO.getSharingMode();
            String sharingMode;

            if ("All threads".equals(sharingModeWeb)) {
                sharingMode = "shareMode.all";
            } else if ("Current thread group".equals(sharingModeWeb)) {
                sharingMode = "shareMode.group";
            } else if ("Current thread".equals(sharingModeWeb)) {
                sharingMode = "shareMode.thread";
            } else {
                sharingMode = "shareMode.thread"; // 默认值，可以根据需要更改
            }


            Element cSVDataSet = hashTree.addElement("CSVDataSet");
            cSVDataSet.addAttribute("guiclass", "TestBeanGUI");
            cSVDataSet.addAttribute("testclass", "CSVDataSet");
            cSVDataSet.addAttribute("testname", filename);
            cSVDataSet.addAttribute("enabled", "true");

            /** Filename */
            Element filenameElement = cSVDataSet.addElement("stringProp");
            filenameElement.addAttribute("name", "filename");
            filenameElement.setText(filename);

            /** File encoding */
            Element fileEncodingElement = cSVDataSet.addElement("stringProp");
            fileEncodingElement.addAttribute("name", "fileEncoding");
            fileEncodingElement.setText(fileEncoding);

            /** Variable names */
            Element variableNamesElement = cSVDataSet.addElement("stringProp");
            variableNamesElement.addAttribute("name", "variableNames");
            variableNamesElement.setText(variableNames);

            /** Ignore first line */
            Element ignoreFirstLineElement = cSVDataSet.addElement("boolProp");
            ignoreFirstLineElement.addAttribute("name", "ignoreFirstLine");
            ignoreFirstLineElement.setText(ignoreFirstLine);

            /** Delimiter */
            Element delimiterElement = cSVDataSet.addElement("stringProp");
            delimiterElement.addAttribute("name", "delimiter");
            delimiterElement.setText(delimiter);

            /** Allow quoted data */
            Element quotedDataElement = cSVDataSet.addElement("boolProp");
            quotedDataElement.addAttribute("name", "quotedData");
            quotedDataElement.setText(allowQuotedData);

            /** Recycle on EOF */
            Element recycleElement = cSVDataSet.addElement("boolProp");
            recycleElement.addAttribute("name", "recycle");
            recycleElement.setText(recycleOnEof);

            /** Stop thread on EOF */
            Element stopThreadElement = cSVDataSet.addElement("boolProp");
            stopThreadElement.addAttribute("name", "stopThread");
            stopThreadElement.setText(stopThreadOnEof);

            /** Sharing mode */
            Element shareModeElement = cSVDataSet.addElement("stringProp");
            shareModeElement.addAttribute("name", "shareMode");
            shareModeElement.setText(sharingMode);

            /** 每个csv后面都要加一行<hashTree/> 最后一个好像不用加，但加了貌似也没问题 */
            hashTree.addElement("hashTree");
        }
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
//        String filePath = "jmx/out.jmx";
//        try {
//            System.out.println(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")).readLine().replace(" ", ""));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        jmx.addCsv("a.csv", "/home/a.csv", "a,b,c", JMeterSampleEnum.HTTP_REQUEST.getCode());
//        jmx.writeJmxFile("jmx/out.jmx");
        JMeterXMLService jmx = new JMeterXMLService();
        jmx.init("docker/jmx/thread_group_http.jmx");
        jmx.addHttpBody("{\"one\": 1}");
        jmx.writeJmxFile("docker/jmx/out.jmx");
    }
}
