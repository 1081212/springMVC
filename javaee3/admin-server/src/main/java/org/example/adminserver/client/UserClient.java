package org.example.adminserver.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Component 将该接口放入ioc容器
 * @FeignClient(name = "user-server",fallback = UserClientFalback.class)
 * 使用feign name必须是服务名称，英文feign底层调用的是ribbon，而riggon底层是通过服务名称和端口号去找到的被调用方的服务
 * fallback 是当被调用服务崩溃或出现其他情况暂时不能回应调用方服务时，设置默认返回值的降级处理类
 */
@Component
@FeignClient(name = "user-server",fallback = UserClientFalback.class)
public interface UserClient {
    @GetMapping("/user-server/getAllUser/{pageNum}")
    String getAllUser(@PathVariable("pageNum") int pageNum);

    @PostMapping("/user-server/updateUser")
    String updateUser();

}
