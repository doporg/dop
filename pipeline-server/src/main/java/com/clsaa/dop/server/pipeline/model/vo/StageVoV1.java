package com.clsaa.dop.server.pipeline.model.vo;

import lombok.*;

import java.util.ArrayList;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageVoV1 {
    private String name;

    private ArrayList<StepVoV1> steps;
}
