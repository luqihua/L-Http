package com.lu.http.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @Author: luqihua
 * @Time: 2018/6/8
 * @Description: 生成xxx.HttpClient类以及它的方法
 */

public class ClientCreate {


    public static TypeSpec.Builder getBuilder() {
        TypeSpec.Builder httpClientBuilder = TypeSpec.classBuilder("HttpClient")
                .addModifiers(Modifier.PUBLIC);

        MethodSpec methodSpec = MethodSpec.methodBuilder("_newInstance")
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(TypeVariableName.get("T"))
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(String.class, "objName", Modifier.FINAL).build())
                .addCode("try{\n")
                .addStatement(" $T cl = Class.forName(objName)", Class.class)
                .addStatement(" return (T) cl.newInstance()")
                .addCode("} catch (ClassNotFoundException e) {\n" +
                        "   e.printStackTrace();\n" +
                        "} catch (IllegalAccessException e) {\n" +
                        "   e.printStackTrace();\n" +
                        "} catch (InstantiationException e) {\n" +
                        "   e.printStackTrace();\n" +
                        "}\n")
                .addStatement("return null")
                .build();

        httpClientBuilder.addMethod(methodSpec);

        return httpClientBuilder;
    }

    public static FieldSpec createClientField(TypeElement typeElement) {
        final String varName = StringUtils.capitalize(typeElement.getSimpleName().toString().toLowerCase());
        final String filedName = "s" + varName;
        return FieldSpec.builder(ClassName.get(typeElement), filedName, Modifier.PUBLIC, Modifier.STATIC)
                .build();
    }

    public static MethodSpec createClientMethod(TypeElement typeElement) {
        final String varName = StringUtils.capitalize(typeElement.getSimpleName().toString().toLowerCase());
        final String filedName = "s" + varName;
        return MethodSpec.methodBuilder("get" + varName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(typeElement))
                .beginControlFlow("if($L==null)", filedName)
                .addStatement(" $L = _newInstance($T.class.getCanonicalName() + $S)", filedName, ClassName.get(typeElement), "Impl")
                .endControlFlow()
                .addStatement("return $L", filedName)
                .build();
    }
}
