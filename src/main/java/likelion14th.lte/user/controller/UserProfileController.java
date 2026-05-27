package likelion14th.lte.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion14th.lte.global.api.ApiResponse;
import likelion14th.lte.global.api.SuccessCode;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/profile")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileController {
    public final UserProfileService userProfileService;
// 각 계층은 자기 역할만 해야 한다는 게 단일 책임 원칙임.Controller의 역할은 클라이언트 요청을 받아서 응답을 돌려주는 것. 즉 "누가 뭘 요청했는지" 만 처리하면 됨. DB에서 어떻게 데이터를 찾고 가공하는지는 Controller가 알 필요가 없음.만약 Controller에서 Repository를 직접 호출하면 나중에 유저를 찾는 로직이 바뀔 때 Controller도 뜯어고쳐야 함. 그리고 같은 로직이 여러 Controller에 중복으로 흩어질 수도 있음. Service에 몰아두면 로직이 바뀌어도 Service만 고치면 끝임.
    @GetMapping
    @Operation(summary = "유저 프로필 조회", description = "유저 아이디를 받아 유저 프로필을 반환하는 api입니다.")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @RequestParam Long userId
    ){
        UserProfileResponse userProfileResponse = userProfileService.getUserProfile(userId);

        return ApiResponse.onSuccess(SuccessCode.OK, userProfileResponse);
    }

    @PostMapping
    @Operation(summary = "테스트 유저를 생성", description = "이름, 한 줄 소개, 유저 태그")
    public ApiResponse<UserProfileResponse> createTestUserProfile(
            // @RequestBody가 붙어있으면 Spring이 이 JSON 텍스트를 읽고 CreateTestUserRequest 객체로 자동 변환해줌. 이 변환 작업은 Spring 내부에 있는 Jackson이라는 라이브러리가 담당함.
            //Jackson이 JSON의 키 이름을 보고 CreateTestUserRequest의 필드 이름과 매칭시켜서 값을 집어넣는 구조임. "username" 이면 username 필드에, "introduction" 이면 introduction 필드에 넣는 식으로. 덕분에 개발자가 JSON을 직접 파싱하는 코드를 짤 필요가 없음.
            @RequestBody CreateTestUserRequest createTestUserRequest
    ){
        UserProfileResponse response = userProfileService.createTestUser(createTestUserRequest);
                return ApiResponse.onSuccess(SuccessCode.CREATED, response);
    }
}
