package com.qzmagic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qzmagic.service.impl.GenTableServiceImpl;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class DynamicMapperController {
    private static final Logger LOG = LoggerFactory.getLogger(GenTableServiceImpl.class);
    /**
     * 存储执行方法
     */
    private final static Map<String, Method> mapperMethods = new ConcurrentHashMap<>();

    /**
     * 存储MySQL实例
     */
    private final static Map<String, Object> mapperInstances = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @PostConstruct
    public void registerDynamicControllers() throws NoSuchMethodException {
        Map<String, Object> mappers = applicationContext.getBeansWithAnnotation(Mapper.class);
        List<String> collect = Arrays.stream(Object.class.getMethods()).map(Method::getName).collect(Collectors.toList());
        List<String> classCollect = Arrays.stream(Proxy.class.getMethods()).map(Method::getName).collect(Collectors.toList());
        for (Object mapper : mappers.values()) {
            Method[] methods = mapper.getClass().getMethods();
            for (Method method : methods) {
                if (!collect.contains(method.getName()) && !classCollect.contains(method.getName())) {
                    // 基于方法名创建路由
                    String path = "/" + method.getName();
                    // 创建处理器方法（反射调用）
                    Method handlerMethod = DynamicMapperController.class.getMethod("handleMapperMethod", HttpServletRequest.class, HttpServletResponse.class);
                    // 注册路由
                    PatternsRequestCondition patterns = new PatternsRequestCondition(path);
                    RequestMethodsRequestCondition methodsCondition = new RequestMethodsRequestCondition(RequestMethod.GET);
                    RequestMappingInfo mappingInfo = new RequestMappingInfo(patterns, methodsCondition, null, null, null, null, null);
                    requestMappingHandlerMapping.registerMapping(mappingInfo, this, handlerMethod);
                    mapperMethods.put(path, method);
                    // 保存Mapper实例的引用
                    mapperInstances.put(path, mapper);
                }
            }
        }
    }



    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        // 设置文档信息，如标题、描述、版本等
        Info info = new Info()
                .title("Dynamic Mapper API")
                .version("v1.0")
                .description("API documentation for dynamically generated mapper endpoints");
        openAPI.info(info);


        // 遍历动态生成的路由并为每个路由创建一个PathItem
        mapperMethods.forEach((path, method) -> {
            PathItem pathItem = new PathItem();
            Operation operation = new Operation(); // 创建一个Operation对象来描述API操作
            // 设置Operation的信息，如摘要、描述、标签等
            operation.summary("Auto-generated method for " + method.getName());

            // 创建一个用于描述参数的Object Schema
            ObjectSchema paramSchema = new ObjectSchema();
            // 添加参数、请求体和响应等信息
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                // 为每个参数创建并设置对应的属性
                paramSchema.addProperty("p" + (i + 1),  createSchemaForParameter(parameter));
            }

            // 创建OpenAPI的参数对象
            Content content = new Content();
            // 设置参数类型
            MediaType mediaType = new MediaType().schema(paramSchema); // 假设所有参数都是对象类型
            content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
            RequestBody requestBody = new RequestBody().content(content).required(true);
            AtomicInteger index= new AtomicInteger();
            requestBody.setDescription("Auto-generated param by : "+
                    Arrays.stream(parameters).map(res->{
                        index.getAndIncrement();
                        return "p"+index.get()+":"+res.getType().getSimpleName();
                    }).collect(Collectors.joining(";"))
            );
            operation.requestBody(requestBody);

            // 根据HTTP方法类型设置对应的操作
            pathItem.get(operation); // 假设为GET请求

            // 将PathItem添加到OpenAPI定义中
            openAPI.path(path, pathItem);
        });

        return openAPI;
    }




    public ResponseEntity<?> handleMapperMethod(HttpServletRequest request, HttpServletResponse response) {
        Method mapperMethod = mapperMethods.get(request.getRequestURI());
        if (mapperMethod != null) {
            Object result;
            try {
                result = mapperMethod.invoke(mapperInstances.get(request.getRequestURI()), resolveMethodArguments(mapperMethod, request));
            } catch (Exception e) {
                LOG.info("invoke method error: {}", e.getMessage());
                return ResponseEntity.ok(Optional.ofNullable(e.getMessage()).orElse("Buddha can't save you anymore, the code is reporting an error"));
            }
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    public Object[] resolveMethodArguments(Method method, HttpServletRequest request) throws IOException {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        ServletInputStream inputStream = request.getInputStream();
        if (inputStream.available() != 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(inputStream, Map.class);
            // 根据请求头中的键映射参数
            for (int i = 0; i < paramTypes.length; i++) {
                String paramName = "p" + (i + 1); // 构造参数名
                Object arg =map.get(paramName); // 从请求体中获取参数值
                args[i] = objectMapper.convertValue(arg, paramTypes[i]); // 转换参数类型
            }
        }

        return args;
    }


    private Schema<?> createSchemaForParameter(Parameter parameter) {
        Type type = parameter.getParameterizedType();
        Class<?> clazz = parameter.getType();

        if (clazz.isArray()) {
            // 数组类型
            return new ArraySchema().items(createSchemaByClass(clazz.getComponentType()));
        } else if (List.class.isAssignableFrom(clazz)) {
            // List类型
            Type genericType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (genericType instanceof Class) {
                return new ArraySchema().items(createSchemaByClass((Class<?>) genericType));
            }
        }
        // 非数组和List类型
        return createSchemaByClass(clazz);
    }


    private Schema<?> createSchemaByClass(Class<?> clazz) {
        // 根据参数类型创建Schema
        if (String.class.equals(clazz)) {
            return new StringSchema();
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            return new IntegerSchema();
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return new BooleanSchema();
        } else {
            // 其他类型
            return new Schema<>().type("object"); // 默认为object类型
        }
    }
}