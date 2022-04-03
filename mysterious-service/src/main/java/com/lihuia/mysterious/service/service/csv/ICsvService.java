package com.lihuia.mysterious.service.service.csv;

import com.lihuia.mysterious.core.vo.csv.CsvQuery;
import com.lihuia.mysterious.core.vo.csv.CsvVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
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
     */
    void uploadCsv(Long testCaseId, MultipartFile csvFile);

    /**
     * 新增CSV文件
     * @param csvVO
     */
    void addCsv(CsvVO csvVO);

    /**
     * 删除CSV文件
     * @param id
     */
    void deleteCsv(Long id);

    /**
     * 更新CSV文件
     * @param csvVO
     */
    void updateCsv(CsvVO csvVO);

    /**
     * 分页查询CSV文件列表
     * @param csvQuery
     * @return
     */
    PageVO<CsvVO> getCsvList(CsvQuery csvQuery);

    /**
     * 查询用例关联的所有CSV文件
     * @param testCaseId
     * @return
     */
    List<CsvVO> getByTestCaseId(Long testCaseId);

    /**
     * getById
     * @param id
     * @return
     */
    CsvVO getDetail(Long id);

}
