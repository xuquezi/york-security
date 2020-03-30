package com.example.york.swagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration//让Spring来加载该类配置
@EnableSwagger2//注解来启用Swagger2。
public class Swagger2 {
   /*
    GET 获取
    PUT 更新
    POST 新增
    PATCH 部分更新 由于有的浏览器兼容性问题，一般推荐使用put
    DELETE 删除
    */
   //swagger地址：http://localhost:1201/york/swagger-ui.html
    /*@Bean
    public Docket createRestApi() {//通过createRestApi函数创建Docket的Bean
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()//select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                .apis(RequestHandlerSelectors.basePackage("com.example.york.controller"))//扫描的包路径
                .paths(PathSelectors.any())
                .build();
    }*/

    /*目前我们的swagger页面并没有让我们输入token的文本框，因此我们需要改造Swagger2Config*/
    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.york.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());

    }


    //创建该Api的基本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("York APIs")
                .description("York项目的APIs")
                .contact("天下不易其乐")
                .version("1.0")
                .build();
    }
/*  Api：修饰整个类，描述Controller的作用
    ApiOperation：描述一个类的一个方法，或者说一个接口
    ApiParam：单个参数描述
    ApiModel：用对象来接收参数
    ApiProperty：用对象接收参数时，描述对象的一个字段
    ApiResponse：HTTP响应其中1个描述
    ApiResponses：HTTP响应整体描述
    ApiIgnore：使用该注解忽略这个API
    ApiError ：发生错误返回的信息
    ApiImplicitParam：一个请求参数
    ApiImplicitParams：多个请求参数*/
}

/*
paramType="body" 代表参数应该放在请求的什么地方：

    header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
    query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
    path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解)
    body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
    form（不常用）
*/
