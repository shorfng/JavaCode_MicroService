package com.loto.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {
    // RestTemplate 模板对象（Rest 风格的远程服务调用）
    @Autowired
    private RestTemplate restTemplate;

    // 注入服务发现客户端
    @Autowired
    private DiscoveryClient discoveryClient;

    //http://localhost:8090/autodeliver/checkState1/1545132
    /**
     * 原始写法
     */
    @GetMapping("/checkState1/{userId}")
    public Integer findResumeOpenState1(@PathVariable Long userId) {
        // 调用远程服务（简历微服务接口） - RestTemplate
        return restTemplate.getForObject("http://localhost:8080/resume/openstate/" + userId, Integer.class);
    }

    //http://localhost:8090/autodeliver/checkState2/1545132
    /**
     * 服务注册到 Eureka 之后的改造：从注册中心拿服务实例，进行访问
     */
    @GetMapping("/checkState2/{userId}")
    public Integer findResumeOpenState2(@PathVariable Long userId) {
        // 1、从 Eureka Server 中获取 resume-service-resume 服务的实例信息（使用客户端对象做这件事）
        List<ServiceInstance> instances = discoveryClient.getInstances("resume-service-resume");

        // 2、如果有多个实例，选择一个使用(负载均衡的过程)
        ServiceInstance serviceInstance = instances.get(0);

        // 3、从元数据信息获取 host、port
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        String url = "http://" + host + ":" + port + "/resume/openstate/" + userId;
        System.out.println("===============>>>从EurekaServer集群获取服务实例拼接的url：" + url);

        // 调用远程服务（简历微服务接口） - RestTemplate
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    //http://localhost:8090/autodeliver/metadata
    /**
     * 获取 Eureka 元数据
     */
    @GetMapping("/metadata")
    public void testInstanceMetadata() {
        List<ServiceInstance> instances = discoveryClient.getInstances("resume-service-resume");

        // 自定义的元数据的值
        System.out.println(instances.get(0).getMetadata().get("cluster"));
        System.out.println(instances.get(0).getMetadata().get("region"));
        System.out.println("=============================");

        // 所有实例
        for (int i = 0; i < instances.size(); i++) {
            ServiceInstance serviceInstance = instances.get(i);
            System.out.println(serviceInstance);
        }
    }

    //http://localhost:8090/autodeliver/checkState3/1545132
    /**
     * 使用 Ribbon 负载均衡
     */
    @GetMapping("/checkState3/{userId}")
    public Integer findResumeOpenState3(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * Hystrix 使用 - 跳闸机制
     */
    // 使用 @HystrixCommand 注解进行熔断控制
    @HystrixCommand(
            // 熔断的一些细节属性配置
            commandProperties = {
                    // 每一个属性都是一个 HystrixProperty
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            }
    )
    @GetMapping("/checkState4/{userId}")
    public Integer findResumeOpenState4(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * Hystrix 使用 - 回退机制（熔断后回退，返回预设默认值）
     */
    @HystrixCommand(
            // 熔断的一些细节属性配置
            commandProperties = {
                    // 每一个属性都是一个 HystrixProperty
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            },
            fallbackMethod = "myFallBack"  // 回退方法
    )
    @GetMapping("/checkState5/{userId}")
    public Integer findResumeOpenState5(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * 定义回退方法，返回预设默认值（该方法形参和返回值与原始方法保持一致）
     */
    public Integer myFallBack(Long userId) {
        return -123333; // 兜底数据
    }

    /**
     * Hystrix 使用 - 舱壁模式（线程池隔离策略)
     */
    @HystrixCommand(
            // 线程池标识，要保持唯一，不唯一的话就共用了
            threadPoolKey = "findResumeOpenState6",

            // 线程池细节属性配置
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"), // 线程数
                    @HystrixProperty(name = "maxQueueSize", value = "20") // 等待队列长度
            },

            // 熔断的一些细节属性配置
            commandProperties = {
                    // 每一个属性都是一个 HystrixProperty
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            },
            fallbackMethod = "myFallBack"  // 回退方法
    )
    @GetMapping("/checkState6/{userId}")
    public Integer findResumeOpenState6(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * Hystrix 使用 - 熔断策略（跳闸后自我修复）
     */
    @HystrixCommand(
            // 线程池标识，要保持唯一，不唯一的话就共用了
            threadPoolKey = "findResumeOpenState7",

            // 线程池细节属性配置
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"), // 线程数
                    @HystrixProperty(name = "maxQueueSize", value = "20") // 等待队列长度
            },

            // 熔断的一些细节属性配置
            commandProperties = {
                    // 每一个属性都是一个 HystrixProperty
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),

                    // Hystrix 熔断策略（高级配置，定制工作过程细节）
                    // 8秒钟内，请求次数达到2个，并且失败率在50%以上，就跳闸，跳闸后活动窗⼝设置为3s
                    // 统计时间窗口定义
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "8000"),
                    // 统计时间窗口内的最小请求数
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    // 统计时间窗口内的错误数量百分比阈值
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 自我修复时的活动窗口长度
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000")
            },
            fallbackMethod = "myFallBack"  // 回退方法
    )
    @GetMapping("/checkState7/{userId}")
    public Integer findResumeOpenState7(@PathVariable Long userId) {
        // 指定服务名
        String url = "http://resume-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }
}
