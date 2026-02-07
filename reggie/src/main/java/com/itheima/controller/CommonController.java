package com.itheima.controller;

import com.itheima.reggie.common.CustomeException;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

//通用控制器
//作用:放置多个业务都想需要控制
//1. 上传
//2. 下载
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    //1:创建变量保存图片目录
    //2:创建上传控制器
    // http://127.0.0.1:8080/common/upload
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //上传文件信息
        //log.info(file.getOriginalFilename()
        //+":"+file.getSize());
        log.info("基础路径:{}",basePath);
        //1:获取原始文件名，截取后缀 abc.jpg  .jpg
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(
                originalFilename.lastIndexOf("."));
        //2:通过uuid生成新名字  abc.jpg  didueiidudd9e8e8dj.jpg
        String fileName = UUID.randomUUID().toString()+suffix;
        //3:创建文件对象(上传目录)
        File dir = new File(basePath);
        //4:判断当前文件对象中目录是否存在
        //5:不存在则创建
        if(!dir.exists()){
            dir.mkdirs();  //如果不存在此目录，多层创建
        }
        log.info("上传目录,{}",dir.toString());
        //6:将用户上传文件转存指定目录（文件移动）
        //临时文件保存上传图片文件-->转存-->新目录
        try{
            file.transferTo(new File(basePath + "/" +fileName));
        }catch(IOException e){
            e.printStackTrace();
        }
        //7:上传成功!!!!!!!!
        return R.success(fileName);
    }

    //文件下载
    /**
     *
     * @param name       下载图片名称
     * @param response   发送图片对象
     * 作用:
     *    1:下载图片  d:\java\ upload\1111.jpg --> upload.html
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info(basePath+"/"+name);
        try {
            //任务一：读取服务器文件
            FileInputStream fis =
                    new FileInputStream(new File(basePath+ "/"
                            +name));
            //依靠此对象向浏览器(write)发送
            ServletOutputStream sout =
                    response.getOutputStream();
            //任务二：指定下载文件类型
            response.setContentType("image/jpeg");
            //任务三: 创建数组分批次发送（代码多一次） 1024字节数组
            //       默认一次读一个字节 1025[1024,1] -1
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = fis.read(buff)) != -1){
                sout.write(buff,0,len);
                sout.flush();
            }
            //任务四：关闭资源
            sout.close();
            fis.close();
            //任务五：处理异常
            log.info("输出结束");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("下载异常 {}",e.getMessage());
            throw new CustomeException(e.getMessage());
        }
    }
}