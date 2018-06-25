package com.lu.http.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @Author: luqihua
 * @Time: 2018/6/8
 * @Description: AMethodCreate
 */

public abstract class AMethodCreate {

    /**
     * 根据方法元素生成每个接口方法公共的代码
     *
     * @param executableElement
     * @return
     */
    protected MethodSpec.Builder getBuilder(ExecutableElement executableElement) {

        //返回值类型
        TypeMirror returnType = executableElement.getReturnType();
        //因为返回值类型是形如 Observable<HttpResult<UserInfo>> ,因此我们需要得到HttpResult<UserInfo>
        if (returnType instanceof DeclaredType) {
            DeclaredType type = (DeclaredType) returnType;
            returnType = type.getTypeArguments().get(0);
        }

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(executableElement.getReturnType()));

        // 生成代码 final Type _type = new TypeToken<HttpResult<Bean>>(){}.getType();
        methodBuilder.addStatement("final $T _type = new $T<$T>(){}.getType()", Type.class,
                ClassName.get("com.google.gson.reflect", "TypeToken")
                , ClassName.get(returnType));

        // 生成代码 Map<String,String> _params = new HashMap<>();
        methodBuilder.addStatement("final $T<String,String> _params = new $T<>()", Map.class, HashMap.class);

        // 生成代码 final HttpHeaderMap _headers = new HttpHeaderMap();
        ClassName headerType = ClassName.get("com.lu.rxhttp.obj", "HttpHeaderMap");
        methodBuilder.addStatement("final $T _headers = new $T()", headerType, headerType);

        return methodBuilder;
    }


    public abstract MethodSpec createMethod(ExecutableElement executableElement);
}
