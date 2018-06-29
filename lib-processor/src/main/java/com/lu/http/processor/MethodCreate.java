package com.lu.http.processor;

import com.lu.http.annotation.Body;
import com.lu.http.annotation.ContextType;
import com.lu.http.annotation.FileMap;
import com.lu.http.annotation.FileParam;
import com.lu.http.annotation.LMethod;
import com.lu.http.annotation.Header;
import com.lu.http.annotation.LRequest;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.ProgressListener;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * @Author: luqihua
 * @Time: 2018/6/8
 * @Description: FormMethodCreate
 */

public class MethodCreate {

    /**
     * 根据方法元素生成每个接口方法公共的代码
     *
     * @param executableElement
     * @return
     */

    public MethodSpec createMethod(ExecutableElement executableElement) {
        LRequest LRequest = executableElement.getAnnotation(LRequest.class);
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
        // 处理url
        String url = LRequest.value();
        methodBuilder.addStatement("String _url = $S", url)
                .beginControlFlow("if(_url.length()==0)")
                .addStatement("throw new $T(\"incorrect url\")", RuntimeException.class)
                .endControlFlow()
                .beginControlFlow("if(!_url.startsWith(\"http\"))")
                .addStatement("_url = _baseUrl+_url")
                .endControlFlow();


        // 生成代码 final Type _type = new TypeToken<returnType>(){}.getType();
        methodBuilder.addStatement("final $T _type = new $T<$T>(){}.getType()", Type.class,
                ClassName.get("com.google.gson.reflect", "TypeToken")
                , ClassName.get(returnType));

        // 生成代码 Map<String,String> _params = new HashMap<>();
        methodBuilder.addStatement("final $T<String,String> _params = new $T<>()", Map.class, HashMap.class);

        // 生成代码 Map<String,String> _headers = new HashMap<>();
        methodBuilder.addStatement("final $T<String,String> _headers = new $T<>()", Map.class, HashMap.class);

        //如果是MULTIPART  则添加一个Map<String,File> _fileMap = new HashMap<>();用于存放文件数据
        if (LRequest.type() == ContextType.MULTIPART) {
            methodBuilder.addStatement("$T<String,$T> _fileMap = new $T<>()", Map.class, File.class, HashMap.class);
        }

        //解析方法的形参
        for (VariableElement variableElement : executableElement.getParameters()) {
            //形参的名称
            final String parameterName = variableElement.getSimpleName().toString();
            //形参传入实现类的方法
            methodBuilder.addParameter(ClassName.get(variableElement.asType()), parameterName);

            Header header = variableElement.getAnnotation(Header.class);
            //将头信息放入_headers
            if (header != null) {
                methodBuilder.addStatement("_headers.put($S,$L)", header.value(), parameterName);
                continue;
            }

            //将单个的参数键值对放入_params
            Param param = variableElement.getAnnotation(Param.class);
            if (param != null) {
                methodBuilder.addStatement("_params.put($S,$L)", param.value(), parameterName);
                continue;
            }

            //将参数集合放入_params
            ParamMap paramMap = variableElement.getAnnotation(ParamMap.class);
            if (paramMap != null) {
                methodBuilder.addStatement("_params.putAll($L)", variableElement.getSimpleName().toString());
                continue;
            }
            //如果是上传文件的   处理文件参数
            if (LRequest.type() == ContextType.MULTIPART) {
                FileParam fileParam = variableElement.getAnnotation(FileParam.class);
                if (fileParam != null) {
                    methodBuilder.addStatement("_fileMap.put($S,$L)", fileParam.value(), parameterName);
                }

                FileMap fileMap = variableElement.getAnnotation(FileMap.class);
                if (fileMap != null) {
                    methodBuilder.addStatement("_fileMap.putAll($L)", parameterName);
                }

                ProgressListener listener = variableElement.getAnnotation(ProgressListener.class);
                if (listener!=null){
                    methodBuilder.addStatement("final $T _listener = $L",
                            ClassName.get("com.lu.http.Interface","IProgressListener"),
                            parameterName);
                }
            } else if (LRequest.type() == ContextType.JSON) {
                Body jsonBody = variableElement.getAnnotation(Body.class);
                if (jsonBody != null) {
                    methodBuilder.addStatement("final $T _jsonBody= $L", Object.class, parameterName);
                }
            }
        }

        final String targetMethod = LRequest.type().name().toLowerCase();

        if (LRequest.type() == ContextType.MULTIPART) {
            methodBuilder.addStatement("return ($T)_request.$L(_url,_headers,_params,_fileMap,_listener,_type)",
                    executableElement.getReturnType(),
                    targetMethod);
        } else if (LRequest.type() == ContextType.FORM) {
            methodBuilder.addStatement("return ($T)_request.$L(_url,$T.$L,_headers,_params,_type)",
                    executableElement.getReturnType(),
                    targetMethod,
                    LMethod.class,
                    LRequest.method());
        } else if (LRequest.type() == ContextType.JSON) {
            methodBuilder.addStatement("return ($T)_request.$L(_url,_headers,_params,_jsonBody,_type)",
                    executableElement.getReturnType(),
                    targetMethod);
        }

        return methodBuilder.build();
    }
}
