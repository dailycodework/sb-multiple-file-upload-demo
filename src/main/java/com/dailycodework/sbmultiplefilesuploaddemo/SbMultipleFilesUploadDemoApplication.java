package com.dailycodework.sbmultiplefilesuploaddemo;

import com.dailycodework.sbmultiplefilesuploaddemo.upload.IFileUploadService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbMultipleFilesUploadDemoApplication  implements CommandLineRunner {

    @Resource
    private IFileUploadService fileUploadService;

    public static void main(String[] args) {
        SpringApplication.run(SbMultipleFilesUploadDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileUploadService.init();
    }
}
