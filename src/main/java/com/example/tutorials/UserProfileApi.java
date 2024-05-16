package com.example.tutorials;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tutorials.data.UserProfile;
import com.example.tutorials.data.UserProfileRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

@RestController
public class UserProfileApi {

    BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),
    500,
    0.01);

    @Autowired
    UserProfileRepository userProfileRepository;


    @GetMapping("/user/check")
    @ResponseBody
    public String check(@RequestParam("username") String userName) throws JsonParseException, JsonMappingException, IOException{
        if(bloomFilter.mightContain(userName)){
                return "User Name already exists!";
        }else{
            bloomFilter.put(userName);
        }

        return "User Name is not found";

    }

    @PostMapping("/user")
    @ResponseBody
    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) throws IOException{
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 500, 0.01);
        filter.put(1000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        filter.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        userProfile.setBloomFilter(bytes);
        return userProfileRepository.save(userProfile);
    
    }


    @GetMapping("/user/{id}")
    @ResponseBody
    public UserProfile getUserProfile(@PathVariable("id")int id) throws IOException{
        UserProfile userProfile = userProfileRepository.findById(id).get();
        System.out.println(userProfile.getBloomFilter());
        ByteArrayInputStream in = new ByteArrayInputStream(userProfile.getBloomFilter());
        BloomFilter<Integer> filter1 = BloomFilter.readFrom(in, Funnels.integerFunnel());
        System.out.println(filter1.approximateElementCount());
        System.out.println(filter1.expectedFpp());
        return userProfile;
    }


}
