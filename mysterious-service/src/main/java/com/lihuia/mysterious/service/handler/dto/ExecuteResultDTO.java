package com.lihuia.mysterious.service.handler.dto;

import com.lihuia.mysterious.service.redis.TestCaseRedisService;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/4/14 10:05 PM
 */

@Data
public class ExecuteResultDTO extends ResultDTO {

    private TestCaseRedisService redisService;
}
