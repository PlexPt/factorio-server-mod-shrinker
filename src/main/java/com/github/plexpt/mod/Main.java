package com.github.plexpt.mod;


import org.zeroturnaround.zip.ZipInfoCallback;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pengtao
 * @email plexpt@gmail.com
 * @date 2022-01-04 11:10
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
//        if (args.length > 0) {
//            String path = args[0];
//            File file = new File(path);
//            if (file.exists()) {
//            }
//        }
        boolean delete = false;
        if (args != null) {
            List<String> argList = Arrays.asList(args);
            if (argList.contains("-d")) {
                delete = true;
            }
        }
        shrunk(delete);

    }

    private static void shrunk(boolean delete) {
        String jarPath = getJarPath();
        List<String> files = getFiles(jarPath);
        log.info("处理目录：" + jarPath);
        log.info("文件数：" + files.size());

        for (String file : files) {
            if (!file.endsWith(".zip")) {
                log.info("跳过：" + file);
                continue;
            }
            log.info("开始处理：" + file);
            final File zip = new File(file);
            final List<String> names = new ArrayList();


            ZipUtil.iterate(zip, new ZipInfoCallback() {
                public void process(ZipEntry entry) {
                    String name = entry.getName();
                    //name.contains("/graphics/") &&
                    if (name.endsWith(".png")) {
                        names.add(name);
                    }
                    if (name.contains(".git")) {

                        log.warn("GIT目录不应该存在 " + name);
                    }
                }
            });
            if (delete) {
                ZipUtil.removeEntries(zip, names.toArray(new String[0]));
                log.info("添加 " + names);
                log.info("删除 " + file);
            }
        }
    }

    public static String getJarPath() {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1);
        }
        if (path.contains(".jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File dic = new File(path);
        File[] tempList = dic.listFiles();
        if (null == tempList || tempList.length == 0) {
            return files;
        }
        for (File file : tempList) {
            if (file != null) {
                files.add(file.toString());
            }
        }
        return files;
    }

    public static List<String> getFiles() {
        String jarPath = getJarPath();
        List<String> files = getFiles(jarPath);
        return files;
    }

    private static void getAll() {

    }
}
