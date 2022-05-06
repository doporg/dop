package cn.com.devopsplus.dop.server.defect.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@TableName("model")
@Getter
@Setter
public class Model {
    //@TableId
    @TableId(value = "modelName")
    private String modelName;

    @TableId(value = "userId")
    private Integer userId;
    @TableField(value = "modelProject")
    private String modelProject;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "gitUrl")
    private String gitUrl;

}
