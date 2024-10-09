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
     * @param file 文件对象
     * @param dir  保存的前置路径
     * @throws IOException
     */
    public static String saveFile(MultipartFile file, String dir, String publishUrlPrefix) throws IOException {
        return saveFile(file, dir, publishUrlPrefix, null);
    }

    /**
     * @param file 文件对象
     * @throws IOException
     */
    public static String saveFile(MultipartFile file) throws IOException {
        return saveFile(file, null, null, null);
    }


    /**
     * @param file     文件对象
     * @param dir      保存的前置路径
     * @param fileName 再系统内保存的文件名称
     * @return 保存文件相关的信息 该对象默认填写了uuid
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

        String name = file.getOriginalFilename();//真实名称
        String suffixName = name.substring(name.lastIndexOf("."));//后缀名
        suffixName = suffixName.toLowerCase(); //后缀名采用小写

        String datePath = DateUtil.format(new Date(), "yyyy-MM").replace("-", File.separator);
        String rootFilePath = dir + File.separator + datePath + File.separator;
        String publishUrl = (publishUrlPrefix == null ? "" : publishUrlPrefix) + File.separator + datePath + File.separator;
        //由于linux系统中可能存在中文文件乱码，所以不使用中文存储
        String fileId = IdUtil.fastSimpleUUID();
        String publishName = DateUtil.format(new Date(), "yyyyMMdd") + "_" + fileId + suffixName;
        File touchFile = cn.hutool.core.io.FileUtil.touch(rootFilePath + publishName);
        file.transferTo(touchFile);
        return publishUrl + publishName;
    }


}
