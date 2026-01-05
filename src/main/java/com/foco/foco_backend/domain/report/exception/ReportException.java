package com.foco.foco_backend.domain.report.exception;

import com.foco.foco_backend.global.code.BaseErrorCode;
import com.foco.foco_backend.global.exception.CustomException;

public class ReportException extends CustomException {
  public ReportException(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
