package mybatisPlus.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2020年01月06日
 * @see
 * @since 1.0
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @TableLogic
    private int isDeleted;
    private GradeEnum gradle;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
}
