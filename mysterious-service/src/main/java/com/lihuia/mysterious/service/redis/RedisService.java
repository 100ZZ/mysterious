package com.lihuia.mysterious.service.redis;

import com.lihuia.mysterious.service.service.testcase.ITestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lihuia.com
 * @date 2022/4/14 9:05 PM
 */

@Slf4j
@Service
public class RedisService {

    private final String CASE_REDIS_KEY = "mysterious:testcase_ids";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ITestCaseService testCaseService;

    public void pushCaseIdToList(Long caseId) {
        redisUtils.leftPush(CASE_REDIS_KEY, String.valueOf(caseId));
    }

    private Long popCaseIdFromList() {
        String caseId = redisUtils.rightPop(CASE_REDIS_KEY);
        return caseId == null ? null : Long.valueOf(caseId);
    }

    public void startCaseFromRedis() {
        Long caseId = popCaseIdFromList();
        if (caseId != null) {
            log.info("压测结束,从缓存中获取用例id执行,用例id={}", caseId);
            testCaseService.runTestCase(caseId, null);
        }
    }

    /** 取消排队 */
    public Long deleteFromList(Long caseId) {
        return redisUtils.deleteFromList(CASE_REDIS_KEY, String.valueOf(caseId));
    }

    public boolean listContainsCaseId(Long caseId) {
        return redisUtils.listContainsValue(CASE_REDIS_KEY, String.valueOf(caseId));
    }
}
