package com.github.brave2chen.springbooteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.brave2chen.springbooteasy.SpringBootEasyApplication;
import com.github.brave2chen.springbooteasy.model.FlywaySchemaHistory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author chenqy28
 * @date 2020-05-29
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootEasyApplication.class)
class FlywaySchemaHistoryServiceTest {
    @Autowired
    FlywaySchemaHistoryService service;

    @Test
    public void select() {
        int count = service.count();
        assertTrue(count > 0);

        // 分页查询测试
        Page<FlywaySchemaHistory> page = service.page(new Page<>(1, 10));
        Assertions.assertThat(page.getRecords()).isNotEmpty();

        page = service.page(new Page<>(2, count));
        Assertions.assertThat(page.getRecords()).isEmpty();
    }
}