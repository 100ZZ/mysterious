package com.lihuia.mysterious.service.handler.result;

import com.lihuia.mysterious.service.enums.TestCaseStatus;
import com.lihuia.mysterious.service.handler.dto.ResultDTO;
import com.lihuia.mysterious.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lihuia.com
 * @date 2023/4/14 10:25 PM
 */

@Slf4j
public class StopResultHandler extends ResultHandler {

    private RedisService redisService;

    public StopResultHandler(ResultDTO resultDTO) {
        super(resultDTO);
        this.testCaseDO = resultDTO.getTestCaseDO();
        this.testCaseMapper = resultDTO.getTestCaseMapper();
        this.redisService = resultDTO.getRedisService();
    }

    @Override
    public void onProcessComplete(final int exitValue) {
        if (TestCaseStatus.RUN_ING.getCode().equals(testCaseDO.getStatus())) {
            testCaseDO.setStatus(TestCaseStatus.RUN_SUCCESS.getCode());
            testCaseMapper.update(testCaseDO);
        }

        super.onProcessComplete(exitValue);
        redisService.startCaseFromRedis();
    }
}
