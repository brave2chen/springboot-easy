package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.github.brave2chen.springbooteasy.SpringBootEasyApplication;
import com.github.brave2chen.springbooteasy.core.BaseController;
import com.github.brave2chen.springbooteasy.core.BaseEntity;
import com.github.brave2chen.springbooteasy.core.BaseMapper;
import com.github.brave2chen.springbooteasy.core.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 代码自动生成 service
 *
 * @author brave2chen
 * @date 2020-05-29
 */
@Service
public class CodeGeneratorService {

    @Value("${spring.flyway.url}")
    private String url;

    @Value("${spring.flyway.user}")
    private String user;

    @Value("${spring.flyway.password}")
    private String password;


    public boolean generate(String... tables) {
        Assert.notEmpty(tables, "[Assertion failed] - tables argument is required; it must not be empty");

        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        GlobalConfig globalConfig = getGlobalConfig();
        StrategyConfig strategy = getStrategy();
        PackageConfig packageConfig = getPackageConfig();

        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setStrategy(strategy);
        autoGenerator.setPackageInfo(packageConfig);

        TemplateConfig templateConfig = new TemplateConfig();
        autoGenerator.setTemplate(templateConfig);

        // 不生成xml
        templateConfig.setXml(null);
        // 不生成service
        templateConfig.setService(null);

        // 设置tables
        strategy.setInclude(tables);

        // 生成entity, mapper, service, controller
        autoGenerator.execute();
        return true;
    }


    private PackageConfig getPackageConfig() {
        PackageConfig pc = new PackageConfig();
        pc.setParent(SpringBootEasyApplication.class.getPackage().getName());
        pc.setModuleName(null);
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setXml("mapper");
        pc.setService("service");
        pc.setServiceImpl("service");
        pc.setController("controller");
        return pc;
    }

    private StrategyConfig getStrategy() {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setChainModel(true);
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.setRestControllerStyle(true);

        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperEntityColumns("id", "create_by", "create_time");
        strategy.setSuperMapperClass(BaseMapper.class.getName());
        strategy.setSuperServiceImplClass(BaseServiceImpl.class);
        strategy.setSuperControllerClass(BaseController.class);
        return strategy;
    }

    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(this.url);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername(this.user);
        dataSourceConfig.setPassword(this.password);
        return dataSourceConfig;
    }

    private GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(SpringBootEasyApplication.class.getResource("").getPath().split("/target/")[0] + "/src/main/java");
        globalConfig.setOpen(false);
        globalConfig.setAuthor("brave2chen");
        globalConfig.setSwagger2(true);
        globalConfig.setIdType(IdType.AUTO);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setServiceImplName("%sService");

        return globalConfig;
    }
}
