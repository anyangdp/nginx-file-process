# nginx-file-process
nginx文件处理demo（服务器系统cenos 7.4）
## 安装要求
1.nginx版本：1.14.0
2.nginx upload module安装 （nginx必须编译安装，不可以yum安装)   
3.安装方法百度自行搜索，安装包可以在git仓库中下载（此包，我添加了上传模块，路径：nginx-1.14.0/home)   
## nginx文件上传设置（nginx.conf）  
###
```
#文件下载
location /maifeng {
            alias /nas1/file/maifeng/;
            #关闭目录结构（视情况是否打开）
            autoindex off;
        }
#文件上传        
location /upload {
            client_max_body_size 50m;
            # 转到后台处理URL 
            upload_pass @maifeng;
            # 临时保存路径 (暂时保存此处，使用回调处理，将临时文件变成真实有效文件)
            #     # 可以使用散列
            upload_store /tmp/nginx-upload;
            upload_pass_args on;
            # 上传文件的权限，rw表示读写 r只读 
            upload_store_access user:rw;
            # 这里写入http报头，pass到后台页面后能获取这里set的报头字段
            upload_set_form_field "${upload_field_name}_name" $upload_file_name;
            upload_set_form_field "${upload_field_name}_content_type" $upload_content_type;
            upload_set_form_field "${upload_field_name}_path" $upload_tmp_path;
            # Upload模块自动生成的一些信息，如文件大小与文件md5值 
            upload_aggregate_form_field "${upload_field_name}_md5" $upload_file_md5;
            upload_aggregate_form_field "${upload_field_name}_size" $upload_file_size;
            # 允许的字段，允许全部可以 "^.*$"
            #upload_pass_form_field "^submit$|^description$";
            upload_pass_form_field "^.*$";
            # 每秒字节速度控制，0表示不受控制，默认0 
            upload_limit_rate 0;
            # 如果pass页面是以下状态码，就删除此次上传的临时文件 
            upload_cleanup 400 404 499 500-505;                                                                                
}
# proxy_pass 不支持uri添加／（可以使用alias），下面配置等同于访问：http://localhost:7992/maifeng        
location @maifeng {
            rewrite ^ /maifeng$1 break;
            proxy_pass  http://localhost:7992;
}
```
## 回调处理项目部署（这里使用了java，网上有使用lua等等，经过我的测试，并不是特别好用，环境配置很麻烦，因为我的服务器已有jre，所以我就直接jar方式部署)    
1.maven 打包（会生成两个包，有boot的那个支持jar方式运行部署）     
2.nohup java -jar demo.jar > demp.txt 2>&1 & (jar方式部署命令，容器部署自行解决)
