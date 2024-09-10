package com.lihuia.mysterious.service.handler.dto;

import com.lihuia.mysterious.core.entity.report.ReportDO;
import com.lihuia.mysterious.core.entity.testcase.TestCaseDO;
import com.lihuia.mysterious.core.mapper.report.ReportMapper;
import com.lihuia.mysterious.core.mapper.testcase.TestCaseMapper;
import com.lihuia.mysterious.service.redis.RedisService;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @author lihuia.com
 * @date 2023/4/14 9:01 PM
 */

@Data
public class ResultDTO {

    private ByteArrayOutputStream outputStream;

    private ByteArrayOutputStream errorStream;

    private TestCaseDO testCaseDO;

    private ReportDO reportDO;

    private TestCaseMapper testCaseMapper;

    private ReportMapper reportMapper;

    private RedisService redisService;
}
