package com.lihuia.mysterious.service.service.jar.impl;

import com.lihuia.mysterious.core.vo.jar.JarQuery;
import com.lihuia.mysterious.core.vo.jar.JarVO;
import com.lihuia.mysterious.core.vo.page.PageVO;
import com.lihuia.mysterious.service.service.jar.IJarService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/1 下午4:36
 */

@Service
public class JarService implements IJarService {
    @Override
    public Boolean uploadJar(Long testCaseId, MultipartFile jarFile) {
        return null;
    }

    @Override
    public Long addJar(JarVO jarVO) {
        return null;
    }

    @Override
    public Boolean updateJar(JarVO jarVO) {
        return null;
    }

    @Override
    public Boolean deleteJar(Long id) {
        return null;
    }

    @Override
    public PageVO<JarVO> getJarList(JarQuery jarQuery) {
        return null;
    }

    @Override
    public List<JarVO> getByTestCaseId(Long testCaseId) {
        return null;
    }
}
