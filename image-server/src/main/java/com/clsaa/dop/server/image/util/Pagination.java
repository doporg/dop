package com.clsaa.dop.server.image.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 用于返回自定义的分页信息
 * @param <T>
 * @author xzt
 * @since 2019-4-16
 */
@Getter
@Setter
@NoArgsConstructor
public class Pagination<T> {
    private List<T> contents;
    private int totalCount;
}
