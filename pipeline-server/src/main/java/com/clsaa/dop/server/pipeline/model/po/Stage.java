package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stage {

    @SerializedName("id")
    private Long Id;

    @SerializedName("name")
    private String name;

    @SerializedName("tasks")
    private ArrayList<Task> tasks;
}
