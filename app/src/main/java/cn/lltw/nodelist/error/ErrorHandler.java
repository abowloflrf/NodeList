package cn.lltw.nodelist.error;

/**
 * Created by ruofeng on 2017/9/27.
 */

public class ErrorHandler {

    public static String convertErrorCode(String code)
    {
        switch (code){
            //TODO:补全错误码提示 https://docs.wilddog.com/auth/Android/api/error-code.html
            case "22009":return "邮箱地址无效";
            case "22005":return "用户创建失败，请重试";
            case "22203":return "邮箱地址已经被其他账户使用";
            case "22211":return "密码的长度必须在 6 到 32 位";
            case "22220":return "邮箱不存在";
            case "22010":return "密码不正确";
            default:return "登陆错误:"+code;
        }
    }
}
