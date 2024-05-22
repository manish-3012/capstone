package com.capstone.ems.domain.dto;

import java.util.List;

import com.capstone.ems.enums.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

	private Long reqId;
    private Long managerId;
    private Long projectId;
    private List<Long> employeeIds;
    private RequestStatus status;
}