package com.github.brave2chen.springbooteasy.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.diboot.core.util.BeanUtils;
import com.github.brave2chen.springbooteasy.config.security.SecurityUser;
import com.github.brave2chen.springbooteasy.core.DataEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis-Plus 配置类
 *
 * @author brave2chen
 * @date 2020-05-29
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(value = MybatisPlusConfig.MAPPER_PACKAGE)
public class MybatisPlusConfig {
    public static final String MAPPER_PACKAGE = "com.github.brave2chen.springbooteasy.mapper";
    public static final int PAGE_SIZE_LIMIT = 1000;

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(false);

        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(PAGE_SIZE_LIMIT);

        // 开启 count 的 join 优化，只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        // 攻击 SQL 阻断解析器: BlockAttackSqlParser
        // 多租户 SQL 解析处理拦截器: TenantSqlParser

        // 加入解析链
        List<ISqlParser> sqlParserList = new ArrayList<>();
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                log.debug("start insert fill ....");
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof SecurityUser) {
                    this.strictInsertFill(metaObject, BeanUtils.convertToFieldName(DataEntity::getCreateBy), Long.class, ((SecurityUser) principal).getId());
                    this.strictInsertFill(metaObject, BeanUtils.convertToFieldName(DataEntity::getUpdateBy), Long.class, ((SecurityUser) principal).getId());
                }

                log.debug("end insert fill ....");
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                log.debug("start update fill ....");
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof SecurityUser) {
                    this.strictUpdateFill(metaObject, BeanUtils.convertToFieldName(DataEntity::getUpdateBy), Long.class, ((SecurityUser) principal).getId());
                }
                log.debug("end update fill ....");
            }
        };
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass);

                // 以下 3 个为内置选装件
                methodList.add(new InsertBatchSomeColumn());
                methodList.add(new AlwaysUpdateSomeColumnById(i -> i.getFieldFill() != FieldFill.UPDATE || i.getFieldFill() != FieldFill.INSERT_UPDATE));
                methodList.add(new LogicDeleteByIdWithFill());

                //增加自定义方法

                return methodList;
            }
        };
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


}
