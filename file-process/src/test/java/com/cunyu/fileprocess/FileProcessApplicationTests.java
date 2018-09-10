package com.cunyu.fileprocess;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileProcessApplicationTests {

    @Test
    public void contextLoads() {
        //创建HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //构建POST请求   请求地址请更换为自己的。
        //1)
        HttpPost httpPost = new HttpPost("http://47.100.55.208/upload");
        InputStream inputStream = null;
        try {
            //文件路径请换成自己的
            //2)
            String content ="hellow 中国";
            inputStream= new ByteArrayInputStream(content.getBytes("UTF-8"));
//            inputStream = new FileInputStream("/Users/say/Desktop/weixin_default.jpeg");
            StringBody stringBody = new StringBody("12",ContentType.MULTIPART_FORM_DATA);
            HttpEntity httpEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("uploadFile", inputStream, ContentType.create("multipart/form-data"), "weixin_default.jpeg")
                    .addPart("id", stringBody)
                    .build();
            httpPost.setEntity(httpEntity);
            //发送请求
            //发送请求
            HttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            if(httpEntity != null){
                inputStream = httpEntity.getContent();
                //转换为字节输入流
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Consts.UTF_8));
                String body = null;
                while ((body = br.readLine()) != null) {
                    System.out.println(body);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
