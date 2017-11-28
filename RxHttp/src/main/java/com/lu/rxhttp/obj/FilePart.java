package com.lu.rxhttp.obj;

import java.io.File;

/**
 * Author: luqihua
 * Time: 2017/11/20
 * Description: FilePart
 */

public class FilePart {
    public String fileName;
    public File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName == null ? file.getName() : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
