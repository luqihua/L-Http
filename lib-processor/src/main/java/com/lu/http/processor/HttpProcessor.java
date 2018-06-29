package com.lu.http.processor;

import com.google.auto.service.AutoService;
import com.lu.http.annotation.ApiService;
import com.lu.http.annotation.LRequest;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.RequestWrapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @Author: luqihua
 * @Time: 2018/6/4
 * @Description: HttpProcessor
 */

@AutoService(Processor.class)
public class HttpProcessor extends AbstractProcessor {

    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mElementUtils = env.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(ApiService.class.getCanonicalName());
        types.add(Param.class.getCanonicalName());
        types.add(ParamMap.class.getCanonicalName());
        types.add(RequestWrapper.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ApiService.class);
        //每个element对应一个接口文件
        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE || !(element instanceof TypeElement))
                continue;
            createSourceFile(roundEnv, element);
        }
        return true;
    }


    private void createSourceFile(RoundEnvironment roundEnv, Element element) {
        //新生成的类命名为接口名+Impl
        final String classImpName = element.getSimpleName().toString() + Constants.IMPL_CLASS_SUFFIX;
        TypeSpec.Builder builder = TypeSpec.classBuilder(classImpName)
                .addModifiers(Modifier.PUBLIC)
                //添加衣蛾全局变量  String _baseUrl
                .addField(String.class, "_baseUrl", Modifier.FINAL, Modifier.PRIVATE)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, "baseUrl", Modifier.FINAL)
                        .addStatement("this._baseUrl=baseUrl")
                        .build())
                //全局变量，一个用于请求http的request，这个request是事先写好的
                .addField(getRequestField(roundEnv))
                .addSuperinterface(TypeName.get(element.asType()));

        //构建方法
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (!(enclosedElement instanceof ExecutableElement) || enclosedElement.getKind() != ElementKind.METHOD) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) enclosedElement;
            //生成接口中的方法
            //必须有GET或者POST注解才能生成代码
            if (executableElement.getAnnotation(LRequest.class) == null) {
                continue;
            }
            //生成方法
            builder.addMethod(new MethodCreate().createMethod(executableElement));
        }

        JavaFile javaFile = JavaFile.builder(mElementUtils.getPackageOf(element).toString(), builder.build())
                .addFileComment(" This codes are generated automatically. Do not modify!")
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FieldSpec getRequestField(RoundEnvironment roundEnv) {

        TypeName requestType = null;

        Set<? extends Element> requestElements = roundEnv.getElementsAnnotatedWith(RequestWrapper.class);
        if (requestElements.size() == 0) {
            requestType = Constants.HTTP_REQUEST;
        } else {
            for (Element element : requestElements) {
                requestType = ClassName.get(element.asType());
                break;
            }
        }

        return FieldSpec.builder(requestType, "_request")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", requestType)
                .build();
    }
}
