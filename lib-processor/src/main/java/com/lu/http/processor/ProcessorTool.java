package com.lu.http.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @Author: luqihua
 * @Time: 2018/6/5
 * @Description: 用于辅助打印一些信息到文件test.Logger
 */

public class ProcessorTool {
    private ProcessingEnvironment processingEnv;
    private List<String> args = new ArrayList<>();
    private List<MethodSpec> methodSpecs = new ArrayList<>();


    public ProcessorTool(ProcessingEnvironment env) {
        this.processingEnv = env;
    }

    public ProcessorTool addArgs(String arg) {
        args.add(arg);
        return this;
    }

    public ProcessorTool addMethod(MethodSpec methodSpec){
        methodSpecs.add(methodSpec);
        return this;
    }

    /**
     * 用于打印log到文件
     */
    public void printLog() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Logger");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("test");

        int len = args.size();
        for (int i = 0; i < len; i++) {
            String arg = args.get(i);
            methodBuilder.addStatement("$T arg" + i + "=$S", String.class, arg);
        }

        for (MethodSpec spec : methodSpecs) {
            builder.addMethod(spec);
        }

        builder.addMethod(methodBuilder.build());

        JavaFile javaFile = JavaFile.builder("test", builder.build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
