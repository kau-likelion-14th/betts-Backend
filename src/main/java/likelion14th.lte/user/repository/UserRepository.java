package likelion14th.lte.user.repository;

import likelion14th.lte.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUserTag(String userTag);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id != :userId AND u.id NOT IN (SELECT f.toUser.id FROM Follow f WHERE f.fromUser.id = :userId)")
    Page<User> findCanFollowUsers(@Param("userId") Long userId, Pageable pageable);
}