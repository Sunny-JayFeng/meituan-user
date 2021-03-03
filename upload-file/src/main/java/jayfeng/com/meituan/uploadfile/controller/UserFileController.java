package jayfeng.com.meituan.uploadfile.controller;

import jayfeng.com.meituan.uploadfile.constant.FilePathEnum;
import jayfeng.com.meituan.uploadfile.response.ResponseMessage;
import jayfeng.com.meituan.uploadfile.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 美团用户文件控制层
 * @author JayFeng
 * @date 2021/2/2
 */
@RestController
@RequestMapping("/meituan/user")
@Slf4j
public class UserFileController extends BaseController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * @param request 请求
     * @return 返回文件在服务器上的地址
     */
    @PostMapping("/uploadFile")
    public ResponseMessage uploadFile(HttpServletRequest request) {
        log.info("uploadFile 上传文件");
        return requestSuccess(fileService.uploadFile(request, FilePathEnum.USER_BASIC_FILE_PATH.basicFilePath()));
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @return 返回删除结果
     */
    @DeleteMapping("/deleteFile")
    public ResponseMessage deleteFile(@RequestParam("fileName") String fileName) {
        log.info("deleteFile 删除文件 fileName: {}", fileName);
        return requestSuccess(fileService.deleteFile(FilePathEnum.USER_BASIC_FILE_PATH.basicFilePath(), fileName));
    }

    /**
     * 批量删除文件
     * @param fileNameList 文件名列表
     * @return 返回删除结果
     */
    @DeleteMapping("/batchDeleteFile")
    public ResponseMessage batchDeleteFile(@RequestBody List<String> fileNameList) {
        log.info("batchDeleteFile 批量删除文件");
        return requestSuccess(fileService.batchDeleteFile(FilePathEnum.USER_BASIC_FILE_PATH.basicFilePath(), fileNameList));
    }

}
