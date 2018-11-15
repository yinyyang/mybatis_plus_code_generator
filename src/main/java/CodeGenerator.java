import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * MyBatis Plus  代码生成器,目前支持模板为:
 * mapper.xml.ftl
 * entity.java.ftl
 * mapper.java.ftl
 * service.java.ftl
 * serviceImpl.java.ftl
 * controller.java.ftl
 * 如果需要自定义代码模板，需要编辑好ftl拷贝到 当前项目resources/templates目录
 * 数据源直接读取项目中的application.properties或application-dev.properties文件
 * 需要配置final String  email 公司 生成代码模板类注解会用到
 *
 */
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) throws Exception {

        final String email = "xxx@gmail.com";
        final String company = "XX";
        final String author = "tiny";
        final String parentPackage="com.demo";
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setSwagger2(true);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        Properties pro = new Properties();

        FileInputStream in = new FileInputStream(new File(projectPath + "/src/main/resources/application-dev.properties").exists()?projectPath + "/src/main/resources/application-dev.properties":projectPath + "/src/main/resources/application.properties");
        pro.load(in);


        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(pro.getProperty("spring.datasource.url"));
        // dsc.setSchemaName("public");
        dsc.setDriverName(pro.getProperty("spring.datasource.driverClassName"));
        dsc.setUsername(pro.getProperty("spring.datasource.username"));
        dsc.setPassword(pro.getProperty("spring.datasource.password"));
        in.close();
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackage);
        //模块名会变成包名组成部分
        String pName = scanner("模块(module)");
        pc.setController("web." + pName);
        pc.setService("service." + pName);
        pc.setServiceImpl("service." + pName + ".impl");
        pc.setMapper("mapper." + pName);
        pc.setEntity("model." + pName);
        //pc.setModuleName("x."+pName);
        mpg.setPackageInfo(pc);

        // 自定义参数配置  代码模板会使用这些参数
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map map = new HashMap<String, Object>();
                map.put("email", email);
                map.put("company", company);
                this.setMap(map);
            }
        };
        //模板配置
        String gType = scanner("是否   只  生成实体(only generate entity)?(Y/N)");
        TemplateConfig templateConfig = new TemplateConfig();
        //mapper xml单独生成
        templateConfig.setXml(null);

        if (gType.equalsIgnoreCase("Y")) {
            templateConfig.setMapper(null);
            templateConfig.setController(null);
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);

        } else if(gType.equalsIgnoreCase("N")) {

            //生成mapper.xml 目录
            List<FileOutConfig> focList = new ArrayList<>();
            focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return projectPath + "/src/main/resources/mybatis/"
                            + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
            cfg.setFileOutConfigList(focList);
        }else{
            return;
        }
        mpg.setCfg(cfg);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //   strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(false);
        strategy.setRestControllerStyle(true);
        // strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        strategy.setTablePrefix("t");
        strategy.setInclude(scanner("表名(table name)"));
        //strategy.setSuperEntityColumns("id");
        strategy.entityTableFieldAnnotationEnable(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }



}