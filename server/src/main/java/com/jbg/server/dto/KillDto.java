package com.jbg.server.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

/**
 * @Author:debug (SteadyJack)
 * @Date: 2019/6/17 22:18
 **/
@Data
@ToString
public class KillDto implements Serializable{

    /**
	 * 序列化
	 */
	private static final long serialVersionUID = 3772138052923118443L;

	@NotNull
    private Integer killId;

    private Integer userId;
}