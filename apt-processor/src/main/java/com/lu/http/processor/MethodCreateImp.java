package com.lu.http.processor;

import com.lu.http.annotation.FileMap;
import com.lu.http.annotation.FileParam;
import com.lu.http.annotation.GET;
import com.lu.http.annotation.Header;
import com.lu.http.annotation.POST;
import com.lu.http.annotation.Param;
import com.lu.http.annotation.ParamMap;
import com.lu.http.annotation.UrlEncoded;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * @Author: luqihua
 * @Time: 2018/6/8
 * @Description: MethodCreateImp
 */

public class MethodCreateImp extends AMethodCreate {

    @Override
    public MethodSpec createMethod(ExecutableElement executableElement) {
        final UrlEncoded encoded = executableElement.getAnnotation(UrlEncoded.class);
        //创建方法
        MethodSpec.Builder methodBuilder = getBuilder(executableElement);
        if (encoded.value() == UrlEncoded.EncodeType.MULTIPART) {
            methodBuilder.addStatement("$T<String,$T> _fileMap = new $T<>()", Map.class, File.class, HashMap.class);
        }
        //方法的形参集合
        List<? extends VariableElement> variableElements = executableElement.getParameters();

        for (VariableElement variableElement : variableElements) {
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
            if (encoded.value() == UrlEncoded.EncodeType.MULTIPART) {
                FileParam fileParam = variableElement.getAnnotation(FileParam.class);
                if (fileParam != null) {
                    methodBuilder.addStatement("_fileMap.put($S,$L)", fileParam.value(), parameterName);
                }

                FileMap fileMap = variableElement.getAnnotation(FileMap.class);
                if (fileMap != null) {
                    methodBuilder.addStatement("_fileMap.putAll($L)", parameterName);
                }
            }
        }

        String method = null;
        if (encoded.value() == UrlEncoded.EncodeType.FORM) {
            method = executableElement.getAnnotation(GET.class) == null ? Constants.REQUEST_METHOD_POST :
                    Constants.REQUEST_METHOD_GET;
        } else if (encoded.value() == UrlEncoded.EncodeType.JSON) {
            method = Constants.REQUEST_METHOD_JSON;
        } else if (encoded.value() == UrlEncoded.EncodeType.MULTIPART) {
            method = Constants.REQUEST_METHOD_POST_MULTIPART;
        }

        if (encoded.value() == UrlEncoded.EncodeType.MULTIPART) {
            methodBuilder.addStatement("return ($T)_request.$L($S,_headers,_params,_fileMap,_type)",
                    ClassName.get(executableElement.getReturnType()),
                    method,
                    executableElement.getAnnotation(POST.class).value());
        } else {
            methodBuilder.addStatement("return ($T)_request.$L($S,_headers,_params,_type)",
                    ClassName.get(executableElement.getReturnType()),
                    method,
                    executableElement.getAnnotation(POST.class).value());
        }

        return methodBuilder.build();
    }
}
