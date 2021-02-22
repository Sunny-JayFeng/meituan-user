package jayfeng.com.meituan.uploadfile.constant;

/**
 * @author JayFeng
 * @date 2021/2/2
 */
public enum FilePathEnum {

    ADMIN_BASIC_FILE_PATH("/opt/local/static/admin/file/"),

    SELLER_BASIC_FILE_PATH("/opt/local/static/seller/file/"),

    USER_BASIC_FILE_PATH("/opt/local/static/user/file/");
//    USER_BASIC_FILE_PATH("E:/Duing/LevelTwo/user/file/");

    private String basicFilePath;

    FilePathEnum(String basicFilePath) {
        this.basicFilePath = basicFilePath;
    }

    public String basicFilePath() {
        return this.basicFilePath;
    }

}
