package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.config.BizCodes;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

/**
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
public class ValidateUtils {

    private static LocalValidatorFactoryBean validatorFactoryBean;

    public static <T> void validate(T t) {
        ensureValidatorNotNull();
        Set<ConstraintViolation<T>> results = validatorFactoryBean.validate(t);
        StringBuilder messageBuilder = new StringBuilder();
        if (isNotEmpty(results)) {
            results.forEach(error -> messageBuilder.append(error.getPropertyPath()).append('[').append(error.getMessage()).append(']'));
            BizAssert.justInvalidParam(BizCodes.INVALID_PARAM.getCode(), messageBuilder.toString());
        }
    }

    private static void ensureValidatorNotNull() {
        if (validatorFactoryBean == null) {
            validatorFactoryBean = Services.of(LocalValidatorFactoryBean.class);
        }
    }
}
