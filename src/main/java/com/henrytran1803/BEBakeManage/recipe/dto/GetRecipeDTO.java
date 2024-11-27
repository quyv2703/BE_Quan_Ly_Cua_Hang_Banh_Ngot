package com.henrytran1803.BEBakeManage.recipe.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetRecipeDTO {
private int id;
private String name;
private List<GetRecipeDetailDTO> detailDTOS;

}
