package com.clsaa.dop.server.test.model.dto;


import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.model.po.InterfaceUrlInfo;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestScript implements Operation{

    /**
     * 请求相关基础信息
     */
    private InterfaceUrlInfo urlInfo;

    /**
     * 请求头部
     */
    @ManyToOne
    private List<RequestHeader> requestHeaders;

    /**
     * 校验测试是否成功的检查点
     */
    private List<RequestCheckPoint> checkPoints;

    /**
     * 请求结果，需要作为参数传递下去的数据
     */
    private List<UrlResultParam> resultParams;


    @Override
    public void run() {
        //todo
        // run request script
    }

    @Override
    public OperationType type() {
        return OperationType.REQUEST;
    }
}
