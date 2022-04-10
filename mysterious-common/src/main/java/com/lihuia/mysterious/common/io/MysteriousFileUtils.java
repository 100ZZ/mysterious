package com.lihuia.mysterious.common.io;

import com.lihuia.mysterious.common.exception.MysteriousException;
import com.lihuia.mysterious.common.response.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lihuia.com
 * @date 2022/4/10 12:39 PM
 */

@Slf4j
@Component
public class MysteriousFileUtils {

    public boolean emptyDir(File file) {
        return file.list().length == 0;
    }

    public boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    public void mkDir(String path) {
        try {
            File file = new File(path);
            FileUtils.forceMkdir(file);
            log.info("创建目录: {}", path);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.MKDIR_ERROR);
        }
    }

    public void rmDir(String path) {
        try {
            File file = new File(path);
            FileUtils.deleteDirectory(file);
            log.info("删除目录: {}", path);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.RMDIR_ERROR);
        }
    }

    public void rmFile(String filePath) {
        try {
            File file = new File(filePath);
            FileUtils.forceDelete(file);
            log.info("删除文件: {}", filePath);
        } catch (FileNotFoundException e) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.RMFILE_ERROR);
        }
    }

    public void mkDirParent(String filePath) {
        try {
            File file = new File(filePath);
            FileUtils.forceMkdirParent(file);
            log.info("创建父级目录: {}", filePath);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.MKDIR_PARENT_ERROR);
        }
    }

    public void uploadFile(String filePath, MultipartFile multipartFile) {
        try {
            File file = new File(filePath);
            FileUtils.forceMkdirParent(file);
            multipartFile.transferTo(file);
            log.info("上传文件: {}", filePath);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.UPLOAD_FILE_ERROR);
        }
    }

    public void copyFile(String srcFilePath, String dstFilePath) {
        try {
            File file = new File(srcFilePath);
            FileUtils.copyFile(file, new File(dstFilePath));
            log.info("拷贝文件: {} => {}", srcFilePath, dstFilePath);
        } catch (IOException e) {
            throw new MysteriousException(ResponseCodeEnum.COPY_FILE_ERROR);
        }
    }

    public boolean isPathEmpty(String path) {
        return new File(path).listFiles().length == 0;
    }

    public void downloadFileFromURL(String url, String savePath, String saveFileName) {
        if (!new File(savePath).isDirectory()) {
            throw new MysteriousException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        String fullFilePath = savePath + File.separator + saveFileName;
        File file = new File(fullFilePath);
        if (file.exists()) {
            file.delete();
        }
        log.info("下载文件,文件保存路径=" + fullFilePath);
        try {
            FileUtils.copyURLToFile(new URL(url), file);
        } catch (Exception e) {
            log.error("下载文件出错,errorInfo: {}", e.getMessage());
        }
    }

    /** 指定目录下找到特定后缀的文件
     * @param directory      目录
     * @param fileExtensions 文件后缀
     * */
    public List<File> findSpecifiedFiles(String directory, String[] fileExtensions) {
        File file = new File(directory);
        if (!file.isDirectory()) {
            return Collections.emptyList();
        }
        return  new ArrayList<>(FileUtils.listFiles(file, fileExtensions, false));
    }

    public void deleteSpecifiedFiles(String directory, String[] fileExtensions) {
        File file = new File(directory);
        if (!file.isDirectory()) {
            return;
        }
        Collection<File> files = FileUtils.listFiles(file, fileExtensions, false);
        files.forEach(File::delete);
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }
}
