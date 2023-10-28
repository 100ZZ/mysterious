package com.lihuia.mysterious.service.handler.dto;

import com.lihuia.mysterious.service.redis.RedisService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author maple@lihuia.com
 * @date 2023/4/14 10:05 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExecuteResultDTO extends ResultDTO {

    private RedisService redisService;
}
