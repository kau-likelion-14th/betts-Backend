package likelion14th.lte.user.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
//기본 생성자가 아예 없으면 JPA가 동작하지 못합니다. 즉 기본 생성자는 JPA를 위해 어쩔 수 없이 열어두는 것입니다.
//그런데 PUBLIC으로 열어버리면 코드 어디서든 빈 껍데기 객체를 만들 수 있게 됩니다.불완전한 User가 시스템을 돌아다닐 수 있기 때문입니다.
//PROTECTED로 설정하면 외부에서는 기본 생성자 호출이 막히고, 실질적인 객체 생성은 @Builder를 통해서만 가능해집니다. JPA에게는 접근을 허용하면서, 일반 비즈니스 코드에서의 불완전한 생성은 차단하는 최소 권한 원칙입니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    //두 군데에 동시에 영향을 줌. DB 레벨에서는 Hibernate가 테이블을 생성할 때 해당 컬럼에 NOT NULL 제약조건을 붙여줌. 애플리케이션 레벨에서는 null 값이 들어오면 DB에 쿼리를 보내기 전에 예외를 발생시킬 수 있음.
    //다만 이것만으로는 자바 코드 레벨의 null 체크가 완벽하지 않음. 그래서 실제로는 @NotNull과 함께 쓰는 경우가 많음. @Column(nullable = false)는 DB 제약조건, @NotNull은 애플리케이션 유효성 검사 담당으로 역할이 다름.
    @Column(nullable = false)
    private String username;

    @Column(length = 16, nullable = false, unique = true)
    private String userTag;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String s3ImageKey;

    @Builder(access = AccessLevel.PUBLIC)
    private User (String username, String userTag, String introduction){
        this.username = username;
        this.userTag = userTag;
        this.introduction = introduction;
    }
//@Setter를 클래스에 붙이면 id, username, userTag 등 절대 바꾸면 안 되는 필드에도 setter가 생겨버림. 통제가 안 되는 상황.
//반면 updateIntroduction()이라는 메서드를 직접 만들면 "사용자가 자기소개를 수정한다"는 의도가 코드에 명확히 드러남. 나중에 소개글 변경 시 이력 저장이나 이벤트 발행 같은 추가 로직이 필요해져도 이 메서드 안에만 넣으면 됨. 객체가 스스로 자신의 상태 변화를 책임지는 구조가 되는 것.
    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }
}