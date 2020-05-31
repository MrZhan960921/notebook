package com.zcq.service.content;


import com.zcq.dao.content.ShareMapper;
import com.zcq.domain.dto.content.ShareDTO;
import com.zcq.domain.dto.user.UserDTO;
import com.zcq.domain.entity.content.Share;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public ShareDTO findById(Integer id) {
        // 获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();

        //用户中心所有实例的信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        //所以用户中心实例的请求地址
        List<String> targetURLS = instances.stream()
                .map(instance -> instance.getUri().toString() + "/users/{id}")
                .collect(Collectors.toList());
        int i = ThreadLocalRandom.current().nextInt(targetURLS.size());

        UserDTO userDTO = restTemplate.getForObject(
                targetURLS.get(i),
                UserDTO.class, userId
        );

        ShareDTO shareDTO = new ShareDTO();
        // 消息的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());

        return shareDTO;
    }


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        // 用HTTP GET方法去请求，并且返回一个对象
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://localhost:8081/users/{id}",
                String.class, 2
        );

        System.out.println(forEntity.getBody());
        // 200 OK
        // 500
        // 502 bad gateway...
        System.out.println(forEntity.getStatusCode());
    }
}

