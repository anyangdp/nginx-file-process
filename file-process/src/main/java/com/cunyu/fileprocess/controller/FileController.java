package com.cunyu.fileprocess.controller;

import com.cunyu.fileprocess.utils.CommonUtil;
import com.cunyu.fileprocess.utils.DateUtils;
import com.cunyu.fileprocess.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @Author anyang
 * 多个回调demo(项目部署在文件存储的服务器，用来处理文件，之后可以自行拓展删除等等一系列功能，或者自己做成一个文件管理的web可视化项目)
 * 这个方法设置为跨域（如果nginx和本项目不在同一个服务器，建议加上跨域处理）
 * @CreateTime 2018/9/5
 * @Des
 */
@Slf4j
@CrossOrigin
@RestController
public class FileController {
    @Value("${maifeng.upload.path}")
    private String maifengFilePath;

    @Value("${cunyu.upload.path}")
    private String cunyuFilePath;

    @Value("${cunyu.yun.prefix}")
    private String cunyuYunPrefix;

    /**
     * 上传回调demo1
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/maifeng")
    public String maifengFilePath(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        String[] fileNameArr = map.get("file_name");
        String filePath = StringUtils.collectionToDelimitedString(Arrays.asList(map.get("file_path")), "");
        String fileName = StringUtils.collectionToDelimitedString(Arrays.asList(fileNameArr), "");
        System.out.println(FileUtil.getSuffix(fileName));
        File file = new File(filePath);
        System.out.println(filePath);
        String newFileName = CommonUtil.getUUID()+"."+FileUtil.getSuffix(fileName);
        /**
         *  移动文件
         */
        try {
            String currentDate = DateUtils.formatDateToString("yyyy-MM-dd", new Date());
            String hostPath = maifengFilePath+ currentDate + "/" +newFileName;
            String publicPath = cunyuYunPrefix +"maifeng/" + currentDate +"/"+ newFileName;
            FileUtils.copyFile(file, new File(hostPath));
            return publicPath;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件{},copy失败",filePath);
            return null;
        }
    }

    /**
     * 上传回调demo2
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/cunyu")
    public String cunyuFilePath(HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        String[] fileNameArr = map.get("file_name");
        String filePath = StringUtils.collectionToDelimitedString(Arrays.asList(map.get("file_path")), "");
        String fileName = StringUtils.collectionToDelimitedString(Arrays.asList(fileNameArr), "");
        File file = new File(filePath);
        String newFileName = "cybiz"+CommonUtil.getUUID()+"."+FileUtil.getSuffix(fileName);
        /**
         *  移动文件
         */
        try {
            String currentDate = DateUtils.formatDateToString("yyyy-MM-dd", new Date());
            String hostPath = cunyuFilePath+ currentDate + "/" +newFileName;
            String publicPath = cunyuYunPrefix +"cunyu/" + currentDate +"/"+ newFileName;
            FileUtils.copyFile(file, new File(hostPath));
            return publicPath;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件{},copy失败",filePath);
            return null;
        }
    }


}
