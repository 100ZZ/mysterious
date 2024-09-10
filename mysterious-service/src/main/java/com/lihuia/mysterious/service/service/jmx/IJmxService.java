package com.lihuia.mysterious.service.service.jmx;

import com.lihuia.mysterious.core.entity.jmx.JmxDO;
import com.lihuia.mysterious.core.vo.jmx.JmxQuery;
import com.lihuia.mysterious.core.vo.jmx.JmxVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import org.apache.commons.exec.CommandLine;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lihuia.com
 * @date 2023/4/1 下午4:38
 */

public interface IJmxService {

    /**
     * 上传用例的JMX脚本
     * @param testCaseId
     * @param jmxFile
     * @param userVO
     * @return
     */
    Boolean uploadJmx(Long testCaseId, MultipartFile jmxFile, UserVO userVO);

    /**
     * 更新JMX脚本
     * @param jmxVO
     */
    Boolean updateJmx(JmxVO jmxVO, UserVO userVO);

    /**
     * 删除JMX脚本
     * @param id
     */
    Boolean deleteJmx(Long id);

    /**
     * 分页查询JMX列表
     * @param query
     * @return
     */
    PageVO<JmxVO> getJmxList(JmxQuery query);

    /**
     * 运行JMX压测脚本
     * @param commandLine
     * @param testCaseId
     * @param reportId
     * @param userVO
     * @return
     */
    Boolean runJmx(CommandLine commandLine, Long testCaseId, Long reportId, UserVO userVO);

    /**
     * 调试JMX压测脚本，只执行调试脚本一次
     * @param commandLine
     * @param testCaseId
     * @param reportId
     * @param userVO
     * @return
     */
    Boolean debugJmx(CommandLine commandLine, Long testCaseId, Long reportId, UserVO userVO);

    /**
     * 停止JMX脚本压测
     * @param commandLine
     * @param testCaseId
     * @param userVO
     * @return
     */
    Boolean stopJmx(CommandLine commandLine, Long testCaseId, UserVO userVO);

    /**
     * 查询用例关联的所有JMX脚本
     * @param testCaseId
     * @return
     */
    JmxVO getByTestCaseId(Long testCaseId);

    /**
     * getJmxDO
     * @param testCaseId
     * @return
     */
    JmxDO getJmxDO(Long testCaseId);

    /**
     * 在线新增JMX脚本
     * @param jmxVO
     */
    Boolean addOnlineJmx(JmxVO jmxVO, UserVO userVO);

    /**
     * 在线编辑时，先要获取所有内容
     * @param id
     * @return
     */
    JmxVO getOnlineJmx(Long id);

    /**
     * 编辑更新在线脚本
     * @param jmxVO
     */
    Boolean updateOnlineJmx(JmxVO jmxVO, UserVO userVO);

    /**
     * 强制删除jmx所有相关的信息，不对外开放，不关注合理性
     * @param id
     */
    Boolean forceDelete(Long id);

    /**
     * getJmxVO
     * @param id
     * @return
     */
    JmxVO getJmxVO(Long id);
}
