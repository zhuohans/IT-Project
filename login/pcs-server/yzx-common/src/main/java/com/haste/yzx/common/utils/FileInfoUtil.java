package com.haste.yzx.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.haste.yzx.common.Constants;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 */
public class FileInfoUtil {

    /**
     * @param file File Object
     * @param dir  Prefix path to save
     * @throws IOException
     */
    public static String saveFile(MultipartFile file, String dir, String publishUrlPrefix) throws IOException {
        return saveFile(file, dir, publishUrlPrefix, null);
    }

    /**
     * @param file File Object
     * @throws IOException
     */
    public static String saveFile(MultipartFile file) throws IOException {
        return saveFile(file, null, null, null);
    }


    /**
     * @param file File Object
     * @param dir Prefix path to save
     * @param fileName The name of the file saved in the system
     * @return Save file related information The object is filled with uuid by default
     * @throws IOException
     */
    public static String saveFile(MultipartFile file, String dir, String publishUrlPrefix, String fileName) throws IOException {
        if (StrUtil.isEmpty(dir)) {
            String osName = System.getProperties().getProperty("os.name");
            if (osName.startsWith("Windows")) {
                dir = Constants.DEFAULT_PATH_WINDOWS;
            } else {
                dir = Constants.DEFAULT_PATH_LINUX;
            }
        }

        if(StrUtil.isEmpty(publishUrlPrefix)){
            publishUrlPrefix = Constants.DEFAULT_PUBLISH_PATH;
        }

        String name = file.getOriginalFilename(); // name
        String suffixName = name.substring(name.lastIndexOf(".")); // Suffix
        suffixName = suffixName.toLowerCase(); // The suffix name should be lowercase

        String datePath = DateUtil.format(new Date(), "yyyy-MM").replace("-", File.separator);
        String rootFilePath = dir + File.separator + datePath + File.separator;
        String publishUrl = (publishUrlPrefix == null ? "" : publishUrlPrefix) + File.separator + datePath + File.separator;
        String fileId = IdUtil.fastSimpleUUID();
        String publishName = DateUtil.format(new Date(), "yyyyMMdd") + "_" + fileId + suffixName;
        File touchFile = cn.hutool.core.io.FileUtil.touch(rootFilePath + publishName);
        file.transferTo(touchFile);
        return publishUrl + publishName;
    }
}
