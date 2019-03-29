package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.model.po.Po;

import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
public class UserUtils {

    public static <PO extends Po> Function<PO, PO> dateAndUser() {
        return po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setCtime(current);
            po.setMtime(current);
            //todo set user
            po.setCuser(110L);
            po.setMuser(110L);
            return po;
        };
    }
}
