package cn.sn.zwcx.material.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by on 2018/6/2 14:50.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 该类是检测手机号码、邮箱等是否有效
 */

public class RegularUtil {
    /**
     * 要更加准确的匹配手机号码只匹配11位数字是不够的，比如说就没有以144开始的号码段，
     * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 　　电信：133、153、180、189、（1349卫通）
     */
    public static boolean isPhoneNumber(String param){
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(param);
        return  matcher.matches();
    }

    /**
     * 判断email格式是否正确
     */
    public static boolean isEmail(String param){
      //  String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{3,3}$";
        Pattern pattern = Pattern.compile(check);
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }
}
