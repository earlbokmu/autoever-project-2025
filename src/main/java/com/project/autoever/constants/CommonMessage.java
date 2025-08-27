package com.project.autoever.constants;

public class CommonMessage {
    private CommonMessage() {
        // 인스턴스화 방지
    }

    // 인증/인가 관련
    public static final String INVALID_CREDENTIALS = "계정 또는 비밀번호가 올바르지 않습니다.";
    public static final String LOGIN_REQUIRED = "로그인이 필요합니다.";
    public static final String RESIGSTER_SUCCESS = "회원가입이 완료되었습니다.";
    public static final String LOGIN_SUCCESS = "로그인에 성공했습니다.";

    // 사용자 관련
    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String DUPLICATE_ACCOUNT = "이미 존재하는 계정입니다.";
    public static final String DUPLICATE_RESIDENT_NUMBER = "이미 존재하는 주민등록번호입니다.";

    // 입력값 검증
    public static final String INVALID_INPUT = "입력값이 올바르지 않습니다.";
    public static final String REQUIRED_FIELD_MISSING = "필수 입력값이 누락되었습니다.";

    // 서버/DB
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 오류가 발생했습니다.";
}
