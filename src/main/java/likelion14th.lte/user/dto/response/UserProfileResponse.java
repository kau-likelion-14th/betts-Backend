package likelion14th.lte.user.dto.response;


import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileResponse {
    private String username;
    private String profileImageUrl;
    private String introduction;
// 1. 보안 문제
//User Entity에는 클라이언트한테 보여주면 안 되는 정보들이 있을 수 있음. 예를 들어 비밀번호, 내부 id, 민감한 개인정보 같은 것들. Entity를 그대로 반환하면 그 필드들이 전부 노출됨. DTO로 변환하면 클라이언트한테 보여줄 것들만 골라서 내보낼 수 있음.
//2. Entity와 API 응답의 분리
//Entity는 DB 구조에 맞춰져 있고, API 응답은 화면에 필요한 형태여야 함. 이 둘은 다를 수 있음. 위 코드에서도 username + "#" + userTag 처럼 DB에는 따로 저장되어 있지만 클라이언트한테는 합쳐서 보내는 경우가 있음. Entity를 그대로 반환하면 이런 가공이 불가능함.
    public static UserProfileResponse from (User user){
        return new UserProfileResponse(
                user.getUsername() + "#" + user.getUserTag(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}
