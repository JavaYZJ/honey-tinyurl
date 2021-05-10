# Honey Tiny URL server

#What?
Honey Tiny URL Server 是一个高性能短链接(短域名)服务，支持自定义短链接，扩展方便。

#架构设计
[短链接服务架构设计方案][1]

#成果

web版本服务地址：http://dwz.honey.red:6688/#/app

![web版本tiny-url][2]

#版本信息

| 版本号  |  更新信息 |
| :------------ |:---------------:| 
| v1.0.0        |   发布tiny url server ,支持自定义短码
| v1.1.0（规划中）      |   发布Java SDK

# 核心扩展接口
AbstractTinyUrl抽象定义了基本的tiny url 维护规则。
默认的DefaultAbstractTinyUrl是基于DB实现的。使用者可以根据具体情况扩展
```java  
   public interface AbstractTinyUrl {

    /**
     * 添加长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    void insertUrl(TinyUrlPo tinyUrl);

    /**
     * 更新长短链接映射关系
     *
     * @param tinyUrl 长短链接映射关系
     */
    void updateUrl(TinyUrlPo tinyUrl);

    /**
     * 获取长链接
     *
     * @param keyword 短链接
     * @param type    短链接类型
     * @return 长链接
     */
    String obtainUrl(String keyword, TinyUrlType type);


    /**
     * 校验长链接是否已经是自定义过的
     *
     * @param url 长链接
     * @return 是否存在
     */
    boolean customMappingHasExist(String url);

    /**
     * 通过长链接获取短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    String obtainTinyUrl(String url);
}
```
CustomTinyUrl是针对自定义短码的接口定义。一般配合CustomTinyUrlStrategy使用
```java
public interface CustomTinyUrl extends AbstractTinyUrl {

    /**
     * 通过长链接自定义短链接
     *
     * @param url     长链接
     * @param tinyUrl 自定义短链接
     * @return 是否成功
     * @throws CustomTinyUrlException 自定义短链接异常
     */
    boolean customTinyUrl(String url, String tinyUrl) throws CustomTinyUrlException;
}
```
CustomTinyUrlStrategy自定义短码策略，默认的DefaultCustomTinyUrlStrategy是双支持策略
```java
public interface CustomTinyUrlStrategy {

    /**
     * 当url已存在时 是否支持自定义短码
     *
     * @return 是否支持
     */
    boolean shouldSupportCustomOnUrlExist();


    /**
     * 是否支持对同一个url进行多次自定义短码
     *
     * @return 是否支持
     */
    boolean shouldSupportMultiCustom();
}
```
UrlMapping定义了映射以及判断接口。默认的DefaultUrlMapping使用布隆过滤器判断是否存在 有一定的误判率。因此做了DB回查做补偿。mapping映射算法用的DB的自增id。
```java
public interface UrlMappin {


    /**
     * 长链接映射成短链接
     *
     * @param url 长链接
     * @return 短链接
     */
    String mapping(String url);


    /**
     * 长链接是否存在
     *
     * @param url 长链接
     * @return 是否存在
     */
    boolean isExists(String url);
}
```

# 现版本服务的restful接口

## 转换短码

> 接口描述 : 将输入的长url转换成短码

| 规则  |  描述 |
| :------------ |:---------------:| 
| 传输方式      | HTTPS       |   
| 请求方法      | GET       | 
| 字符编码      | 统一采用UTF-8编码       | 
| 响应格式      | 统一采用JSON格式     | 

**API地址：/v1/tinyUrl**

接口参数：

| 参数  | 类型  | 描述 |
| :------------ |:---------------:| -----:|
| url      | java.util.String        |   输入的长url |

返回参数：

| 参数  | 类型  | 描述 | 
| :------------ |:---------------:| -----:|
| code      | int        |   返回码； 200表示成功，非0表示出错 |
| msg      | string        |   返回信息；ret非200时表示出错时错误原因 |
| data      | object        |   返回数据(就是短码)；code为200时有意义 |

## 自定义短码

> 接口描述 : 是否支持将输入的长url转换成自定义短码，支持返回true 不支持返回不支持的msg原因

| 规则  |  描述 |
| :------------ |:---------------:| 
| 传输方式      | HTTPS       |   
| 请求方法      | GET       | 
| 字符编码      | 统一采用UTF-8编码       | 
| 响应格式      | 统一采用JSON格式     | 

**API地址：/v1/tinyUrl/vip**  

接口参数：

| 参数  | 类型  | 描述 |
| :------------ |:---------------:| -----:|
| url      | java.util.String        |   输入的长url |
| customTinyUrl      | java.util.String        |   输入的自定义短码 |

返回参数：

| 参数  | 类型  | 描述 | 
| :------------ |:---------------:| -----:|
| code      | int        |   返回码； 200表示成功，非0表示出错 |
| msg      | string        |   返回信息；ret非200时表示出错时错误原因 |
| data      | object        |   返回数据；code为200时有意义 |

  [1]: https://www.zybuluo.com/yangzhijie/note/1791620
  [2]: http://oss.honey.red/public/honey-tiny-url.jpg

