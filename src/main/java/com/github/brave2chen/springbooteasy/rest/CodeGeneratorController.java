package com.github.brave2chen.springbooteasy.rest;

import com.github.brave2chen.springbooteasy.service.CodeGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 代码自动生成 rest服务
 *
 * @author brave2chen
 * @date 2020-05-29
 */

@Api(tags = "代码自动生成 服务")
@RestController
@RequestMapping(value = "/code", produces = MediaType.APPLICATION_JSON_VALUE)
public class CodeGeneratorController {
    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @ApiOperation(value = "自动生成代码", notes = "根据表名生成想要的entity、mapper、service、controller代码")
    @ApiImplicitParam(value = "表名", name = "table", paramType = "query", required = true, example = "1")
    @PostMapping("/generate")
    public boolean generate(@RequestParam String table) {
        return codeGeneratorService.generate(table);
    }
}
