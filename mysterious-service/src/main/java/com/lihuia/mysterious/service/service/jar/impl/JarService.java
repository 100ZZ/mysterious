package com.lihuia.mysterious.service.service.jar.impl;

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
    public void uploadJar(Long testCaseId, MultipartFile jarFile) {

    }

    @Override
    public void addJar(JarVO jarVO) {

    }

    @Override
    public void updateJar(JarVO jarVO) {

    }

    @Override
    public void deleteJar(Long id) {

    }

    @Override
    public PageVO<JarVO> getJarList(String srcName, Long testCaseId, Long creatorId, Integer page, Integer size) {
        return null;
    }

    @Override
    public List<JarVO> getByTestCaseId(Long testCaseId) {
        return null;
    }
}
