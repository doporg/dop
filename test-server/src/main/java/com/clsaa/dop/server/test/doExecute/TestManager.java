package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import com.clsaa.dop.server.test.doExecute.plugin.PluginManager;
import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.util.Services;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 完成测试脚本的执行
 * @author xihao
 * @version 1.0
 * @since 20/03/2019
 */
public class TestManager {

    public static final String FAIL_RESULT = "fail";
    public static final String SUCCESS_RESULT = "success";

    public static final int MAX_TASK_SIZE = 1000;

    private static PluginManager pluginManager;

    private static BlockingQueue<Runnable> tasksHolder;
    private static ThreadPoolExecutor testScriptExecutor;

    static {
        pluginManager = Services.of(PluginManager.class);
        tasksHolder = new ArrayBlockingQueue<>(MAX_TASK_SIZE, false);
        testScriptExecutor = new ThreadPoolExecutor(
                3,
                5,
                5,
                TimeUnit.MINUTES,
                tasksHolder
        );
    }

    public static InterfaceCaseDto execute(InterfaceCaseDto interfaceCaseDto) {
        Long userId = UserManager.getCurrentUserId();
        ExecuteTask executeTask = new ExecuteTask(interfaceCaseDto, userId);
        testScriptExecutor.execute(executeTask);
        return interfaceCaseDto;
    }

    public static String urlHandled(RequestContext requestContext) {
        return pluginManager.url(requestContext);
    }

    public static Map<String, String> requestHeadersHandled(RequestContext requestContext) {
        return pluginManager.headers(requestContext);
    }

}

