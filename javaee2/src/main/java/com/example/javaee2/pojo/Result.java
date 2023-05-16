package com.example.javaee2.pojo;
import lombok.Data;

/**
 * @description: Result类
 * @author: 张大林
 * @date: 3-27
 * @version: v1.0
 * @other:
 * 1、 Lombok提供了一系列注解用以在后台生成模板代码
 * 2、如果你想使数据类尽可能精简，可以使用 @Data 注解。
 * @Data 是 @Getter、 @Setter、 @ToString、 @EqualsAndHashCode 和 @RequiredArgsConstructor 的快捷方式。
 *
 */
/*

 */
@Data
public class Result {
    private Boolean flag;
    private String msg;

    private Integer statusCode;
    private Object data;
    public Result(Boolean flag, String msg,Integer statusCode) {
        this.flag = flag;
        this.msg = msg;
        this.statusCode = statusCode;
    }
    public Result(Boolean flag, String msg,Integer statusCode, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }
}