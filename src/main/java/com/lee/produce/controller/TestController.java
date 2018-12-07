package com.lee.produce.controller;

import com.lee.produce.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.lee.produce.ProduceApplication.KEY_LEE_TEST;

@Api(tags = "消息中间件测试")
@RestController
public class TestController {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Qualifier(value = "exchangeEventTest")
    @Autowired
    private DirectExchange directExchange;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String test(@RequestBody User user) {
      /*  User user = new User();
        user.setId(1);
        user.setName("lee");
        user.setSex(0);
        user.setAge(18);
        user.setHeight(1.65d);*/
        rabbitMessagingTemplate.convertAndSend(directExchange.getName(), KEY_LEE_TEST, user);
        logger.info("test");
        return "hello";
    }
}
