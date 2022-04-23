package com.lihuia.mysterious.service.service.csv;

import com.lihuia.mysterious.core.entity.csv.CsvDO;
import com.lihuia.mysterious.core.vo.csv.CsvParam;
import com.lihuia.mysterious.core.vo.csv.CsvQuery;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.core.vo.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:07
 */

public interface ICsvService {
    /**
     * 上传用例里，脚本依赖的CSV参数文件，必须先上传JMX脚本
     * @param testCaseId
     * @param csvFile
     * @param userVO
     * @return
     */
    Boolean uploadCsv(Long testCaseId, MultipartFile csvFile, UserVO userVO);

    /**
     * 删除CSV文件
     * @param id
     */
    Boolean deleteCsv(Long id);

    /**
     * 更新CSV文件
     * @param csvVO
     */
    Boolean updateCsv(CsvVO csvVO, UserVO userVO);

    /**
     * 分页查询CSV文件列表
     * @param query
     * @return
     */
    PageVO<CsvVO> getCsvList(CsvQuery query);

    /**
     * 查询用例关联的所有CSV文件
     * @param testCaseId
     * @return
     */
    List<CsvVO> getByTestCaseId(Long testCaseId);

    /**
     * getCsvDOList
     * @param testCaseId
     * @return
     */
    List<CsvDO> getCsvDOList(Long testCaseId);

    /**
     * getCsvVO
     * @param id
     * @return
     */
    CsvVO getCsvVO(Long id);
}
