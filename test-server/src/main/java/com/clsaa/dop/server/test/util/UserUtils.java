package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.feign.UserInterface;
import com.clsaa.dop.server.test.model.dto.User;
import com.clsaa.dop.server.test.model.po.Po;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
public class UserUtils {

    private static final ThreadLocal<Long> userThread = ThreadLocal.withInitial(() -> -1L);

    private static UserInterface userInterface;

    static {
        userInterface = Services.of(UserInterface.class);
    }

    public static void setUserId(Long userId) {
        userThread.set(userId);
    }

    public static Long getCurrentUserId() {
        return userThread.get();
    }

    public static void removeThreadUserId() {
        userThread.remove();
    }

    public static User getCurrentUser() {
        return userInterface.getUserById(getCurrentUserId());
    }

    public static <PO extends Po> Function<PO, PO> dateAndUser() {
        return po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setCtime(current);
            po.setMtime(current);
            Long currentUserId = getCurrentUserId();
            po.setCuser(currentUserId);
            po.setMuser(currentUserId);
            return po;
        };
    }
}
