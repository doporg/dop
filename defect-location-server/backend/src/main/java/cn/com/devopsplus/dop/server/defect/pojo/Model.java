package cn.com.devopsplus.dop.server.defect.pojo;

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
    @MppMultiId
    private String modelName;

    @MppMultiId
    private Integer userId;

    private String modelProject;

    private Date createTime;

    private String gitUrl;

}
