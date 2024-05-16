package com.example.tutorials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoggerApi {
    
    @Autowired
    private S3Service service;

    @GetMapping("/log/{userId}")
    @ResponseBody
    public String findLog(@PathVariable("userId")String userId) throws Exception {
        return service.findLogsInS3File(userId);
    }


}
