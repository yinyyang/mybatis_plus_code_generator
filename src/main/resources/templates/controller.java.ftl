package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.springframework.data.redis.core.StringRedisTemplate;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${package.Service}.${table.serviceName};


// swagger address
// http://localhost:8080/swagger-ui.html
@Api(value = "API - ${table.controllerName}", description = "${table.comment!}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private static Logger logger = LoggerFactory.getLogger(${table.controllerName}.class);

	@Autowired
    private ${table.serviceName} service;

    @Autowired
    public StringRedisTemplate redis;
}
</#if>
