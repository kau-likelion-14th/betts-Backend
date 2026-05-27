package likelion14th.lte.user.repository;

import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// 실제 구현체는 Spring 내부에 SimpleJpaRepository라는 클래스가 이미 만들어져 있음. findById, save, delete 같은 기본 기능들이 거기에 다 구현되어 있어서 그냥 가져다 쓰는 구조임.
// findByUsername() 처럼 직접 선언한 메서드는 Spring이 메서드 이름을 읽고 알아서 쿼리를 만들어줌. findBy 뒤에 Username이 붙어있으니까 "username 컬럼으로 찾아라" 라고 자동으로 해석하는 것.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
