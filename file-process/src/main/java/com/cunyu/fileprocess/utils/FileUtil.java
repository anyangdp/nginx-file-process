package com.cunyu.fileprocess.utils;

import lombok.Cleanup;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static String getSuffix(String name) {
        if (CommonUtil.isNotEmpty(name)) {
            if (name.indexOf(".") > 0) {
                return name.substring(name.lastIndexOf(".") + 1, name.length());
            }
        }
        return "";
    }

    /**
     * 创建目录
     *
     * @param path 目录
     * @return true or false
     */
    public static Boolean createFolder(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            return targetFile.mkdirs();
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @return true or false
     */
    public static Boolean createFile(MultipartFile file, String fileName, String filePath, String name) {
        try {
            FileOutputStream out = new FileOutputStream(CommonUtil.toString(filePath, name));
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return new File(CommonUtil.toString(filePath, name)).exists();
    }

    public static Boolean delFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public static String base64ToImgFile(String base64Img) {
        String[] data = StringUtils.split(base64Img, ",");
        String imgName = CommonUtil.getUUID();
        String imgData = "";
        if (CommonUtil.isNotEmpty(data)) {
            if (CommonUtil.isNotEmpty(data[0])) {
                if (data[0].contains("png")) {
                    imgName += ".png";
                } else if (data[0].contains("jpg")) {
                    imgName += ".jpg";
                } else if (data[0].contains("jpeg")) {
                    imgName += ".jpeg";
                } else {
                    imgName += ".jpg";
                }
            }
            if (CommonUtil.isNotEmpty(data[1])) {
                imgData = data[1];
            }
        }
        //对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) //图像数据为空
            return "";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "/tmp/" + imgName;//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            return "";
        }
        return imgName;
    }

    public static void downloadFile(File file, String fileName, HttpServletResponse response) {
        if (file != null) {
            String new_filename = null;
            try {
                new_filename = URLEncoder.encode(fileName, "UTF8");
                InputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new_filename);
                response.addHeader("Content-Length", "" + file.length());
                response.setContentType("application/octet-stream");
                @Cleanup
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                toClient.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] toByteArray(String filePath) throws IOException {
        @Cleanup
        InputStream in = new FileInputStream(filePath);
        @Cleanup
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
