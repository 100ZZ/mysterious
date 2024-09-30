package com.lihuia.mysterious.service.redis;

import com.alibaba.fastjson.JSON;
import com.lihuia.mysterious.core.vo.user.UserVO;
import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lihuia.com
 * @date 2023/4/14 9:05 PM
 */

@Slf4j
@Service
public class RedisService {

    private final String CASE_REDIS_KEY = "mysterious:testcase_id_list";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ITestCaseService testCaseService;

    public void pushTestCaseToList(Long testCaseId, UserVO userVO) {
        try {
            String userJson = JSON.toJSONString(userVO);
            String combinedJson = String.format("{\"testCaseId\":%d,\"userVO\":%s}", testCaseId, userJson);
            redisUtils.leftPush(CASE_REDIS_KEY, combinedJson);
        } catch (Exception e) {
            log.error("Failed to push test case ID and user info to Redis list", e);
        }
    }

    private Map<String, Object> popCaseIdFromList() {
        String combinedJson = redisUtils.rightPop(CASE_REDIS_KEY);
        if (combinedJson == null) {
            return null;
        }
        try {
            return JSON.parseObject(combinedJson, Map.class);
        } catch (Exception e) {
            log.error("Failed to parse test case ID and user info from Redis list", e);
            return null;
        }
    }

    public void startCaseFromRedis() {
        Map<String, Object> combinedInfo = popCaseIdFromList();
        if (combinedInfo != null) {
            Long testCaseId = Long.valueOf(combinedInfo.get("testCaseId").toString());
            UserVO userVO = JSON.parseObject(JSON.toJSONString(combinedInfo.get("userVO")), UserVO.class);
            log.info("压测结束,从缓存中获取用例id执行,用例id={}, 用户信息={}", testCaseId, userVO);
            testCaseService.runTestCase(testCaseId, userVO);
        }
    }

    /** 取消排队 */
    public Long deleteFromList(Long testCaseId) {
        return redisUtils.deleteFromList(CASE_REDIS_KEY, String.valueOf(testCaseId));
    }

    public boolean listContainsCaseId(Long testCaseId) {
        return redisUtils.listContainsValue(CASE_REDIS_KEY, testCaseId);
    }
}
