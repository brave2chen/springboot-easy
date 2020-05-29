package com.github.brave2chen.springbooteasy.rest;

import com.github.brave2chen.springbooteasy.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码自动生成 rest服务
 *
 * @author brave2chen
 * @date 2020-05-29
 */
@RestController
@RequestMapping("/code/")
public class CodeGeneratorController {
    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @GetMapping("generate")
    public boolean generate(String table) {
        return codeGeneratorService.generate(table);
    }
}
