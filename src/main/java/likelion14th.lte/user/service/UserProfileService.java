package likelion14th.lte.user.service;

import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileService {
// new UserRepository()로 직접 생성하면 UserService가 UserRepository에 강하게 묶임. 나중에 UserRepository를 다른 걸로 바꾸고 싶으면 UserService 코드도 직접 뜯어고쳐야 함. DI를 쓰면 외부에서 갈아끼워주는 구조라 UserService는 신경 안 써도 됨.
    private final UserRepository userRepository;
/*@Service
public class UserProfileService {
    private final UserRepository userRepository;

    protected UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}*/
    @Transactional
    public UserProfileResponse createTestUser(CreateTestUserRequest request) {
        // 사용하지 않는다면 각 값이 어떤 의미를 나타내는 파라미터인지 알 수가 없음. 순서도 외워야 함. 파라미터가 많아질수록 더 헷갈림.
        User newUser = User.builder()
                .username(request.getUsername())
                .userTag(request.getUserTag())
                .introduction(request.getIntroduction())
                .build();

        User savedUser;
        try {
            //DB 저장은 여러 작업이 묶인 하나의 덩어리임. @Transactional이 없으면 작업들이 각각 따로 실행됨.
            //저장 도중 DB 서버가 끊겼다고 가정하면, @Transactional이 있으면 중간에 문제가 생겼을 때 지금까지 했던 작업을 전부 롤백해서 없던 일로 만들어줌. @Transactional이 없으면 일부만 저장된 불완전한 데이터가 DB에 남아버릴 수 있음.
            savedUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        return UserProfileResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        return UserProfileResponse.from(user);
    }
}