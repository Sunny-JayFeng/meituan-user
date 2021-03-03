package jayfeng.com.meituan.uploadfile.service;

import jayfeng.com.meituan.uploadfile.exception.UploadFileException;
import jayfeng.com.meituan.uploadfile.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JayFeng
 * @date 2021/2/2
 */
@Service
@EnableScheduling
@Slf4j
public class FileService {

    // 存放要上传到本地的文件的文件流，key为线程id，value这一个请求要上传的所有文件的流
    private Map<Long, List<Map<String, InputStream>>> map = new HashMap<>(8192);
    // 文件都是上传在本地，线程id也是唯一标识，所以可以不用redis
    // 文件上传队列。存放线程 id
    Queue<List<Map<String, InputStream>>> taskQueue = new LinkedList<>();

    // 关闭 io 流
    private void closeIOStream(OutputStream outputStream, InputStream inputStream) {
        if(outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件简单路径名
     * @param originalFileName 原文件名
     * @return 返回简单路径名。如：jpg/test.jpg、png/test.png
     */
    private String createRandomFileSimplePath(String originalFileName) {
        originalFileName = ObjectUtils.isEmpty(originalFileName) ? "" : originalFileName;
        int pointIndex = originalFileName.indexOf(".");
        String suffix = pointIndex == -1 ? "" : originalFileName.substring(pointIndex + 1); // 取文件后缀名
        // 生成存到服务器上的文件名
        boolean haveSuffix = !ObjectUtils.isEmpty(suffix);
        return (haveSuffix ? suffix + "/" : "") + UUID.randomUUID().toString().replaceAll("-", "") + (haveSuffix ? "." + suffix : "");
    }

    /**
     * 每 10 秒去队列里拿一次看需不需要上传文件
     */
    @Scheduled(cron = "0/10 * * * * ?")
    private void uploadFileTaskExecute() {
        List<Map<String, InputStream>> fileList= taskQueue.poll();
        if (!ObjectUtils.isEmpty(fileList)) {
            uploadFile(fileList);
        }
    }

    /**
     * 上传文件任务执行
     * 真正的上传文件逻辑
     * @param fileList 文件列表
     */
    private void uploadFile(List<Map<String, InputStream>> fileList) {
        log.info("uploadFile 开始上传文件，总共要上传 {} 个文件", fileList.size());
        long startTime = System.currentTimeMillis();
        for (Map<String, InputStream> file : fileList) {
            for (String fileAbsolutePath : file.keySet()) {
                String directory = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("/") + 1);
                File directoryFile = new File(directory);
                if (!directoryFile.exists()) {
                    if (directoryFile.mkdirs()) {
                        log.info("uploadFile 文件夹创建失败, 该文件无法上传");
                        break;
                    }
                }
                log.info("uploadFile 上传文件, 文件绝对路径: {}", fileAbsolutePath);
                File newFile = new File(fileAbsolutePath);
                FileOutputStream outputStream = null;
                InputStream inputStream = file.get(fileAbsolutePath);
                try {
                    if (!newFile.createNewFile()) {
                        log.info("uploadFile 文件创建失败, 该文件无法上传");
                        break;
                    }
                    outputStream = new FileOutputStream(newFile);
                    // 压缩图片
                    Thumbnails.of(inputStream).scale(1f).outputQuality(0.1f).toOutputStream(outputStream);
                } catch (IOException e) {
                    StackTraceElement stackTraceElement = e.getStackTrace()[0];
                    log.info("出现异常, 异常类型: {}", e.toString());
                    log.info("异常位置: {} 类的第 {} 行, 出现异常的方法: {}", stackTraceElement.getClassName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName());
                    throw new UploadFileException("服务端错误, 文件上传失败, 请稍后再试");
                } finally {
                    closeIOStream(outputStream, inputStream);
                }
            }
        }
        log.info("uploadFile 文件上传完成, 耗时: {}", System.currentTimeMillis() - startTime);
    }

    /**
     * 提交上传文件任务
     * @param request 请求
     * @param basicFilePath 文件基础路径
     * @return 返回文件在服务器上的地址
     */
    public ResponseData uploadFile(HttpServletRequest request, String basicFilePath) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFileList = multipartRequest.getFiles("files");
        if (ObjectUtils.isEmpty(multipartFileList)) {
            return ResponseData.createFailResponseData("uploadFileInfo", false, "没有文件", "no_have_file");
        }
        // 获取要上传的文件的文件流
        Long threadId = Thread.currentThread().getId();
        List<String> resultList = new ArrayList<>(multipartFileList.size());
        List<Map<String, InputStream>> fileList = new ArrayList<>(multipartFileList.size());
        for (MultipartFile multipartFile : multipartFileList) {
            Map<String, InputStream> tempMap = new HashMap<>(2);
            try {
                String simplePath = createRandomFileSimplePath(multipartFile.getOriginalFilename());
                tempMap.put(basicFilePath + simplePath, multipartFile.getInputStream());
                resultList.add(simplePath);
                fileList.add(tempMap);
            } catch (IOException e) {
                StackTraceElement stackTraceElement = e.getStackTrace()[0];
                log.info("出现异常, 异常类型: {}", e.toString());
                log.info("异常位置: {} 类的第 {} 行, 出现异常的方法: {}", stackTraceElement.getClassName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName());
                throw new UploadFileException("服务端错误, 文件上传失败, 请稍后再试");
            }
        }
        taskQueue.offer(fileList); // 队列push，有任务需要执行
        log.info("uploadFile 上传文件任务已提交");
        return ResponseData.createSuccessResponseData("uploadFileInfo", resultList);
    }

    /**
     * 删除文件
     * @param basicFilePath 文件基础路径
     * @param fileName 文件名
     * @return 返回删除是否成功
     */
    public ResponseData deleteFile(String basicFilePath, String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return ResponseData.createFailResponseData("deleteFileInfo", false, "文件名为空, 无法删除", "file_name_is_empty");
        }
        String absolutePath = basicFilePath + fileName;
        log.info("deleteFile 删除文件, absolutePath: {}, fileName: {}", absolutePath, fileName);
        File file = new File(absolutePath);
        file.delete();
        return ResponseData.createSuccessResponseData("deleteFileInfo", true);
    }

    /**
     * 批量删除文件
     * @param basicFilePath 文件基础路径
     * @param fileNameList 文件名列表
     * @return 返回删除是否成功
     */
    public ResponseData batchDeleteFile(String basicFilePath, List<String> fileNameList) {
        if (fileNameList == null) return ResponseData.createFailResponseData("batchDeleteFileInfo", false, "文件名, 无法删除", "file_name_is_empty");
        // 过滤掉空字符串
        fileNameList = fileNameList.stream().filter(t->!ObjectUtils.isEmpty(t)).collect(Collectors.toList());
        log.info("batchDeleteFile, 批量删除文件 basicFilePath: {}, size: {}", basicFilePath, fileNameList.size());
        if (fileNameList.isEmpty()) return ResponseData.createFailResponseData("batchDeleteFileInfo", false, "文件名, 无法删除", "file_name_is_empty");
        for (String fileName : fileNameList) {
            deleteFile(basicFilePath, fileName);
        }
        return ResponseData.createSuccessResponseData("batchDeleteFileInfo", true);
    }

}
