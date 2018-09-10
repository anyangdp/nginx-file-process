package com.cunyu.fileprocess.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: 通用工具类
 * Author: panyt
 * Version: 1.0
 * Create Date Time: 2017-09-04 9:39.
 * Update Date Time: 2017-09-04 9:39.
 *
 * @see CommonUtil
 */
public class CommonUtil {

    /**
     * 判断对象是否为空
     *
     * @param obj object
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String) return ((String) obj).trim().length() == 0;
        if (obj instanceof Collection) return ((Collection<? extends Object>) obj).size() == 0;
        if (obj instanceof Map) return ((Map<? extends Object, ? extends Object>) obj).size() == 0;
        if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        if (obj instanceof Boolean) return false;
        if (obj instanceof Number) return false;
        if (obj instanceof Character) return false;
        if (obj instanceof Date) return false;
        return false;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj Object
     * @return 对象是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 返回Map
     *
     * @param data data
     * @param <K>  K
     * @param <V>  V
     * @return Map
     */
    public static <K, V> Map<String, V> toMap(Object... data) {
        if (data.length == 1 && data[0] instanceof Map) return (Map<String, V>) data[0];
        if (data.length % 2 == 1) {
            IllegalArgumentException e = new IllegalArgumentException("你必须传入一个键值对应的序列 (长度 = " + data.length + ")");
            throw e;
        }
        Map<String, V> map = new HashMap<String, V>();
        for (int i = 0; i < data.length; ) {
            map.put((String) data[i++], (V) data[i++]);
        }
        return map;
    }

    /**
     * 将两个相同size的list转化为map
     *
     * @param keys   map中的key
     * @param values map中的value
     * @param <K>    key
     * @param <V>    value
     * @return map
     */
    public static <K, V> Map<K, V> createMap(List<K> keys, List<V> values) {
        if (keys == null || values == null || keys.size() != values.size()) {
            throw new IllegalArgumentException("请确认keys和values不为空，并且两个list长度相同（keys:" + keys.size() + ", values:" + values.size() + "）");
        }
        Map<K, V> newMap = new HashMap<K, V>();
        for (int i = 0; i < keys.size(); i++) {
            newMap.put(keys.get(i), values.get(i));
        }
        return newMap;
    }

    /**
     * 将多个object转换为List
     *
     * @param obj object
     * @param <T> 类型
     * @return list
     */
    public static <T> List<T> toList(T... obj) {
        List<T> list = new LinkedList<>();
        if (obj != null && obj.length > 0) {
            for (int i = 0; i < obj.length; ) {
                list.add(obj[i++]);
            }
        }
        return list;
    }

    /**
     * 将特定字符串转化为List
     *
     * @param str 格式为"n1, n2"
     * @return List<String>
     */
    public static List<String> toList(String str) {
        List<String> newList = new LinkedList<String>();
        if (isNotEmpty(str)) {
            if (str.indexOf(",") > 0) {
                String[] entries = str.split(",");
                Collections.addAll(newList, entries);
            } else {
                Collections.addAll(newList, str);
            }
        }
        return newList;
    }

    public static Object[] listToArray(List<Object> list) {
        if (isNotEmpty(list)) {
            return list.toArray(new String[list.size()]);
        }
        return null;
    }

    /**
     * 将N个string拼接成一个string
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String toString(String... str) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (str != null && str.length > 0) {
            for (int i = 0; i < str.length; ) {
                stringBuilder.append(str[i++]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将特定字符串转化为Set
     *
     * @param str 格式为"n1, n2"
     * @return List<String>
     */
    public static Set<String> toSet(String str) {
        Set<String> newSet = new LinkedHashSet<String>();
        if (str.indexOf(",") > 0) {
            String[] entries = str.split(",");
            Collections.addAll(newSet, entries);
        } else {
            Collections.addAll(newSet, str);
        }

        return newSet;
    }

    /**
     * 将字符串分隔为List
     *
     * @param str   原始字符串
     * @param delim 分隔符
     * @return List
     */
    public static List<String> toSplit(String str, String delim) {
        List<String> splitList = null;
        StringTokenizer st;

        if (isEmpty(str)) return null;

        if (isNotEmpty(delim)) st = new StringTokenizer(str, delim);
        else st = new StringTokenizer(str);

        if (st.hasMoreTokens()) {
            splitList = new LinkedList<>();

            while (st.hasMoreTokens())
                splitList.add(st.nextToken());
        }
        return splitList;
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.replace("-", "");
    }

    public static String uuid() {
        String uuid = "";
        for (int i = 0; i < 5; i++) {
            String s = getUUID();
            s = s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
            uuid = s.substring(0, 12);
        }
        return uuid;
    }

    /**
     * MD5加密
     *
     * @param inStr 需加密字符串
     * @return 加密后的字符串
     */
    public static String getMD5(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuilder hexValue = new StringBuilder();

        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static String easyEncrypt(int num) {
        int[] arr = new int[8];
        int index = 0;
        //对数据进行倒叙操作
        while (num > 0) {
            arr[index] = num % 10;
            index++;
            num /= 10;

        }
        //每位数加5后除以10取余
        for (int i = 0; i < index; i++) {
            arr[i] += 5;
            arr[i] %= 10;
        }
        //首尾位置交换
        int temp = arr[index - 1];
        arr[index - 1] = arr[0];
        arr[0] = temp;
        //
        String str = "";//定义一个空数组用来存储连接的字符串
        for (int i = 0; i < index; i++) {
            str += arr[i];//把数据连接成字符串操作
        }
        return str;//返回连接成的字符串
    }

    /**
     * 将object转换为string
     *
     * @param obj object
     * @return String
     */
    public static String getString(Object obj) {
        if (isNotEmpty(obj)) {
            return obj.toString();
        }
        return "";
    }

    /**
     * 将object转换为string
     *
     * @param obj          object
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(Object obj, String defaultValue) {
        if (isNotEmpty(obj)) {
            return obj.toString();
        }
        return defaultValue;
    }

    /**
     * 获取properties文件的单个值
     *
     * @param key      键
     * @param fileName 文件名称
     * @return 值
     */
    public static String getProperty(String key, String fileName, String defaultValue) {
        Properties prop = new Properties();
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + fileName);
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isNotEmpty(prop.getProperty(key))) {
            defaultValue = prop.getProperty(key).trim();
        }
        return getString(defaultValue);
    }

    /**
     * 从project.properties文件中得到数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getProperty(String key, String defaultValue) {
        return getProperty(key, "project.properties", defaultValue);
    }

    /**
     * 从project.properties文件中得到数据
     *
     * @param key 键
     * @return 值
     */
    public static String getProperty(String key) {
        return getProperty(key, "project.properties", "");
    }

    /**
     * 生成编号
     *
     * @param num       编号
     * @param prefixion 前缀
     * @return 编号
     */
    public static String searchUniqueNumber(String num, String prefixion) {
        String number;
        int numLeng = 3;
        if (isNotEmpty(num)) {
            if (num.contains(prefixion)) {
                number = String.valueOf(Integer.parseInt(num.substring(prefixion.length(), num.length())) + 1);
            } else {
                number = "001";
            }
            try {
                if (number.length() >= numLeng) {
                    numLeng += number.length() - numLeng;
                }
                while (number.length() < numLeng) {
                    number = "0" + number;
                }
            } catch (Exception e) {
                number = "001";
            }
        } else {
            number = "001";
        }
        return prefixion + number;
    }

    /**
     * 获取创客端ip
     *
     * @param request request
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 返回解析字段
     *
     * @param flag 操作flag
     * @return 操作
     */
    private static String returnSuccess(Integer flag) {
        switch (flag) {
            case 1:
                return "保存";
            case 2:
                return "更新";
            case 3:
                return "删除";
            case 4:
                return "初始化";
            case 5:
                return "修改状态";
            case 6:
                return "信誉投";
            default:
                return "未知";
        }
    }

    /**
     * 初始化一级菜单，不可编辑，不可修改，不可删除
     *
     * @param id id
     * @return 一级菜单
     */
    public static JSONObject initMenu(String id) {
        JSONObject tree = new JSONObject();
        tree.put("id", id);
        tree.put("pId", "-1");
        tree.put("name", "根目录");
        tree.put("nocheck", true);
        tree.put("isParent", true);
        tree.put("open", true);
        return tree;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (isNotEmpty(result)) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }


    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;
                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source list
     * @return list
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


    /**
     * 页面传入年月日，加入时分秒，开始时间
     *
     * @param date 页面传入参数
     * @return 开始时间
     */
    public static String setStartTime(String date) {
        if (CommonUtil.isNotEmpty(date)) {
            return CommonUtil.toString(date, " 00:00:00");
        }
        return null;
    }

    /**
     * 页面传入年月日，加入时分秒，结束时间
     *
     * @param date 页面传入参数
     * @return 页面结束时间
     */
    public static String setEndTime(String date) {
        if (CommonUtil.isNotEmpty(date)) {
            return CommonUtil.toString(date, " 23:59:59");
        }
        return null;
    }

    /**
     * 得到订单编号
     *
     * @return
     */
    public static String getOrderNo() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String seconds = new SimpleDateFormat("HHmmss").format(new Date());
        return CommonUtil.toString(date, "00001000", getTwo(), "00", seconds, getTwo());

    }

    /**
     * 产生随机的2位数
     *
     * @return
     */
    public static String getTwo() {
        Random rad = new Random();
        String result = rad.nextInt(100) + "";
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }
}